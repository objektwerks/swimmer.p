Swimmer ( Personal Edition )
----------------------------
>Swimmer app using ScalaFx, ScalikeJdbc, H2, HikariCP, Ox, jDeploy and Scala 3.

Install
-------
1. Select [Swimmer](https://www.jdeploy.com/~swimmer-sessions)
2. Select a platform to download a compressed app installer.
3. Decompress app installer.
4. Install app by double-clicking app installer.
5. Select app icon to launch app.
>This install has been tested on macOS.

Model
-----
>A session represents a workout.
* Swimmer 1 ---> * Session

Calculations
------------
1. [Calories Burned Swimming](https://www.calculatorpro.com/calculator/calories-burned-swimming-calculator/)
2. [Distance / Time](https://www.calculatorsoup.com/calculators/math/speed-distance-time-calculator.php)
   
Build
-----
1. ```sbt clean compile```

Test
----
1. ```sbt clean test```

Run
---
1. ```sbt run```

Assembly
--------
1. ```sbt clean test assembly copyAssemblyJar```

Execute
-------
1. ```java -jar .assembly/swimmer-$version.jar```

Deploy
------
1. edit build.sbt ( version )
2. edit app.conf ( about > alert > contentText )
3. edit package.json ( version + jdeploy / jar )
4. sbt clean test assembly copyAssemblyJar
5. perform github release ( from https://github.com/objektwerks/swimmer.p )
6. npm login
7. jdeploy publish ( to https://www.jdeploy.com/~swimmer-sessions )
8. check email for npm message
>See [jDeploy Docs](https://www.jdeploy.com/docs/manual/#_getting_started) for details.

jDeploy Install
---------------
1. Setup npm account at npmjs.com
2. Install node, which installs npm, which bundles npx.
3. Install jdeploy via npm - *npm install -g jdeploy*
4. Add icon.png ( 256x256 or 512x512 ) to project root and resources.
5. Edit jDeploy *package.json* as required.
6. Add *jdeploy* and *jdeploy-bundle* to .gitignore
>See [jDeploy Docs](https://www.jdeploy.com/docs/manual/#_getting_started) for details.

NPM Versioning
--------------
>The ```build.sbt``` **must** contain a ```semver 3-digit``` **version** number. See: [Npmjs Semver](https://docs.npmjs.com/about-semantic-versioning)

NPM Registry
------------
>Swimmer is deployed to: https://www.npmjs.com/package/swimmer-sessions

Resources
---------
* [JavaFX](https://openjfx.io/index.html)
* [JavaFX Tutorial](https://jenkov.com/tutorials/javafx/index.html)
* [ScalaFX](http://www.scalafx.org/)
* [ScalikeJdbc](http://scalikejdbc.org/)

License
-------
>Copyright (c) [2025] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.