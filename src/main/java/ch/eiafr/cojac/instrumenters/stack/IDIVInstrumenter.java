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

package ch.eiafr.cojac.instrumenters.stack;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.LocalVariablesSorter;

import ch.eiafr.cojac.Methods;
import ch.eiafr.cojac.instrumenters.OpCodeInstrumenter;
import ch.eiafr.cojac.reactions.Reaction;

import static org.objectweb.asm.Opcodes.*;

public final class IDIVInstrumenter implements OpCodeInstrumenter {
    @Override
    public void instrument(MethodVisitor methodVisitor, int opCode, String classPath, Methods methods, Reaction reaction, LocalVariablesSorter src) {
        Label label = new Label();
        Label fin = new Label();
        methodVisitor.visitInsn(DUP2);
        methodVisitor.visitInsn(ICONST_M1);
        methodVisitor.visitJumpInsn(IF_ICMPNE, label);
        methodVisitor.visitLdcInsn(Integer.MIN_VALUE);
        methodVisitor.visitJumpInsn(IF_ICMPNE, fin);
        reaction.insertReactionCall(methodVisitor, "Overflow : IDIV", methods, classPath);
        methodVisitor.visitJumpInsn(GOTO, fin);
        methodVisitor.visitLabel(label);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitLabel(fin);
        methodVisitor.visitInsn(IDIV);
    }
}