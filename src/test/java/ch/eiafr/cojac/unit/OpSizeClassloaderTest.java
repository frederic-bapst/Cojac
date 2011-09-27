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

package ch.eiafr.cojac.unit;

import java.net.URL;

import ch.eiafr.cojac.Arg;
import ch.eiafr.cojac.Args;
import ch.eiafr.cojac.CojacClassLoader;
import ch.eiafr.cojac.InstrumentationStats;

public class OpSizeClassloaderTest extends ClassLoaderTest {
    public OpSizeClassloaderTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super();
    }

    @Override
    public ClassLoader getClassLoader(InstrumentationStats stats) {
        Args args = new Args();
        args.specify(Arg.ALL);
        args.specify(Arg.EXCEPTION);
        args.specify(Arg.OP_SIZE);

        return new CojacClassLoader(new URL[0], args, stats);
    }
}