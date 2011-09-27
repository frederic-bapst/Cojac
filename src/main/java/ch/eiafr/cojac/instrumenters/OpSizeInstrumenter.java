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

package ch.eiafr.cojac.instrumenters;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.LocalVariablesSorter;

import ch.eiafr.cojac.Arg;
import ch.eiafr.cojac.Methods;
import ch.eiafr.cojac.Signatures;
import ch.eiafr.cojac.reactions.Reaction;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

final class OpSizeInstrumenter implements OpCodeInstrumenter {
    @Override
    public void instrument(MethodVisitor methodVisitor, int opCode, String classPath, Methods methods, Reaction reaction, LocalVariablesSorter src) {
        Arg arg = Arg.fromOpCode(opCode);

        if (arg == null || !arg.isOperator()) {
            throw new IllegalArgumentException("opCode is not a valid arg");
        }
        
        methodVisitor.visitMethodInsn(INVOKESTATIC, classPath, methods.getMethod(opCode), Signatures.getSignature(arg));
    }
}
