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

package ch.eiafr.cojac.instrumenters.variables;

import ch.eiafr.cojac.Methods;
import ch.eiafr.cojac.instrumenters.OpCodeInstrumenter;
import ch.eiafr.cojac.reactions.Reaction;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.objectweb.asm.Opcodes.*;

public final class FDIVVariablesInstrumenter implements OpCodeInstrumenter {
    @Override
    public void instrument(MethodVisitor mv, int opCode, String classPath, Methods methods, Reaction reaction, LocalVariablesSorter src) {
        int a = src.newLocal(Type.FLOAT_TYPE);
        int b = src.newLocal(Type.FLOAT_TYPE);
        int r = src.newLocal(Type.FLOAT_TYPE);

        mv.visitInsn(DUP2);
        src.visitVarInsn(FSTORE, b);
        src.visitVarInsn(FSTORE, a);
        mv.visitInsn(FDIV);
        src.visitVarInsn(FSTORE, r);
        src.visitVarInsn(FLOAD, a);
        src.visitVarInsn(FLOAD, a);
        mv.visitInsn(FCMPL);
        Label l0 = new Label();
        mv.visitJumpInsn(IFNE, l0);
        src.visitVarInsn(FLOAD, b);
        src.visitVarInsn(FLOAD, b);
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFNE, l0);
        src.visitVarInsn(FLOAD, r);
        src.visitVarInsn(FLOAD, r);
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFEQ, l0);
        reaction.insertReactionCall(mv, "Result is Float.NaN : FDIV", methods, classPath);
        Label l1 = new Label();
        mv.visitJumpInsn(GOTO, l1);
        mv.visitLabel(l0);
        src.visitVarInsn(FLOAD, a);
        mv.visitLdcInsn(new Float("Infinity"));
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFEQ, l1);
        src.visitVarInsn(FLOAD, a);
        mv.visitLdcInsn(new Float("-Infinity"));
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFEQ, l1);
        src.visitVarInsn(FLOAD, b);
        mv.visitLdcInsn(new Float("Infinity"));
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFEQ, l1);
        src.visitVarInsn(FLOAD, b);
        mv.visitLdcInsn(new Float("-Infinity"));
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFEQ, l1);
        src.visitVarInsn(FLOAD, r);
        mv.visitLdcInsn(new Float("Infinity"));
        mv.visitInsn(FCMPL);
        Label l2 = new Label();
        mv.visitJumpInsn(IFNE, l2);
        reaction.insertReactionCall(mv, "Result is Double.POSITIVE_INFINITY : FMUL", methods, classPath);
        mv.visitJumpInsn(GOTO, l1);
        mv.visitLabel(l2);
        src.visitVarInsn(FLOAD, r);
        mv.visitLdcInsn(new Float("-Infinity"));
        mv.visitInsn(FCMPL);
        mv.visitJumpInsn(IFNE, l1);
        reaction.insertReactionCall(mv, "Result is Double.NEGATIVE_INFINITY : FMUL", methods, classPath);
        mv.visitLabel(l1);
        src.visitVarInsn(FLOAD, r);
    }
}