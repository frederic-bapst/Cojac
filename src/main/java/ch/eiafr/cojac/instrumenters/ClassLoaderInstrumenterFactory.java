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

import ch.eiafr.cojac.Arg;
import ch.eiafr.cojac.Args;
import ch.eiafr.cojac.InstrumentationStats;

public final class ClassLoaderInstrumenterFactory implements IOpcodeInstrumenterFactory {
    private final IOpcodeInstrumenter opCodeInstrumenter;

    public ClassLoaderInstrumenterFactory(Args args, InstrumentationStats stats) {
        super();

        if (args.isSpecified(Arg.REPLACE_FLOATS))
            opCodeInstrumenter = new ReplaceFloatsInstrumenter(args, stats);
        else
            opCodeInstrumenter = new DirectInstrumenter(args, stats);
    }

    @Override
    public IOpcodeInstrumenter getInstrumenter(int opcode) {
        if (opCodeInstrumenter.wantsToInstrument(opcode))
            return opCodeInstrumenter;
        return null;
    }
}
