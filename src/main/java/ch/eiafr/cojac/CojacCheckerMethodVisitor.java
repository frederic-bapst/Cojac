/*
 * *
 *    Copyright 2011 Baptiste Wicht & Frédéric Bapst
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ch.eiafr.cojac;

import ch.eiafr.cojac.instrumenters.IOpcodeInstrumenter;
import ch.eiafr.cojac.instrumenters.IOpcodeInstrumenterFactory;
import ch.eiafr.cojac.models.CheckedMaths;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

final class CojacCheckerMethodVisitor extends LocalVariablesSorter {
    private final IOpcodeInstrumenterFactory factory;
    private final InstrumentationStats stats;
    private final Args args;
    private final String classPath;

    private static final List<String> UNARY_METHODS = Arrays.asList(
        "ceil", "round", "floor",
        "cos", "sin", "tan",
        "acos", "asin", "atan",
        "cosh", "sinh", "tanh",
        "exp", "expm1",
        "log", "log10", "log1p",
        "sqrt", "cbrt",
        "rint", "nextUp");

    private static final List<String> BINARY_METHODS =
        Arrays.asList("atan2", "pow", "hypot", "copySign", "nextAfter", "scalb");

    CojacCheckerMethodVisitor(int access, String desc, MethodVisitor mv, InstrumentationStats stats, Args args, String classPath, IOpcodeInstrumenterFactory factory) {
        super(Opcodes.ASM5, access, desc, mv);

        this.stats = stats;
        this.args = args;
        this.factory = factory;

        this.classPath = classPath;
    }

    @Override
    public void visitInsn(int opCode) {
        IOpcodeInstrumenter instrumenter = factory.getInstrumenter(opCode);

        //Delegate to parent
        if (instrumenter == null) {
            super.visitInsn(opCode);
        } else {
            instrumenter.instrument(mv, opCode); //, classPath, methods, reaction, this);
        }
    }

    @Override
    public void visitIincInsn(int index, int value) {
        int opCode=Opcodes.IINC;
        IOpcodeInstrumenter instrumenter = factory.getInstrumenter(opCode);
        if (args.isOperationEnabled(Arg.IINC)) {
            visitVarInsn(ILOAD, index);
            mv.visitLdcInsn(value);
            instrumenter.instrument(mv, opCode);//, classPath, methods, reaction, this);
            visitVarInsn(ISTORE, index);
            stats.incrementCounterValue(Opcodes.IINC); //Arg.IINC);
        } else {
            super.visitIincInsn(index, value);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (opcode == INVOKESTATIC && args.isOperationEnabled(Arg.MATHS) &&
            ("java/lang/Math".equals(owner) || "java/lang/StrictMath".equals(owner))) {
            if ("(D)D".equals(desc) && UNARY_METHODS.contains(name) || "(DD)D".equals(desc) && BINARY_METHODS.contains(name)) {
                String msg= owner + '.' + name + desc;
                String logFileName=""; 
                if (args.isSpecified(Arg.CALL_BACK)) {
                    logFileName = args.getValue(Arg.CALL_BACK); // No, I'm not proud of that trick...
                } else {
                    logFileName = args.getValue(Arg.LOG_FILE);
                }
                int reactionType=args.getReactionType().value();
                mv.visitMethodInsn(INVOKESTATIC, owner, name, desc, itf);
                protectMethodInvocation(reactionType, logFileName, msg);
            } else {
                mv.visitMethodInsn(INVOKESTATIC, owner, name, desc, itf);
            }
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    //checkMathMethodResult(double r, int reaction, 
    //                      String logFileName, String operationName)
    
    private void protectMethodInvocation(int reactionType, String logFileName, String msg) {
        mv.visitInsn(DUP2);
        mv.visitLdcInsn(new Integer(reactionType));
        mv.visitLdcInsn(logFileName);
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(INVOKESTATIC, CheckedMaths.CHECK_MATH_RESULT_PATH, CheckedMaths.CHECK_MATH_RESULT_NAME, Signatures.CHECK_MATH_RESULT, false);
    }
}