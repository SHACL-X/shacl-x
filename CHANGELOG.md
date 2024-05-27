## [1.4.3](https://github.com/SHACL-X/shacl-x/compare/1.4.2...1.4.3) (2024-05-27)


### Dependency updates

* **core-deps:** update dependency org.apache.jena:jena-arq to v4 ([4ebeaea](https://github.com/SHACL-X/shacl-x/commit/4ebeaeaf5dc39a566b68e088c8697a1cdcfd99e8))
* **deps:** update alpine docker tag to v3.20.0 ([dfc9cc7](https://github.com/SHACL-X/shacl-x/commit/dfc9cc722e89bbf78c2176a4e04900cff1b83999))
* **deps:** update dependency maven to v3.9.7 ([b29ef8f](https://github.com/SHACL-X/shacl-x/commit/b29ef8f151cb975751804f0bdec503f6c7c86c65))


### General maintenance

* setting next snapshot version [skip ci] ([fcd86fe](https://github.com/SHACL-X/shacl-x/commit/fcd86fe340a942de1ad73a1946d6152ae7d5279c))
* update code to support jena v4.10 ([f29b525](https://github.com/SHACL-X/shacl-x/commit/f29b525d9c9bf3e5235c806ea5cbde36d9eaa7aa))

## [1.4.2](https://github.com/SHACL-X/shacl-x/compare/1.4.1...1.4.2) (2024-05-24)


### Bug Fixes

* **JSRule:** add check condition for JS triples that are converted to a java.List ([2a180ae](https://github.com/SHACL-X/shacl-x/commit/2a180aede63518f0139388e9fd8a93fbba5c4b67))
* **TermFactory:** term() method accepts an Object instead of a String to avoid Polyglot API cast issues ([a538416](https://github.com/SHACL-X/shacl-x/commit/a53841643728fb40d357ef1f2642e5f05ca29018))


### Tests

* add InferencingTestCaseType ([02403bd](https://github.com/SHACL-X/shacl-x/commit/02403bdc41a94e81de53ab5955cbf8dc230e69cc))
* **inferencing:** add FunctionRegistry code snippet for loading SPARQL functions from the given model ([8e5ae0f](https://github.com/SHACL-X/shacl-x/commit/8e5ae0f8a2687e98c9032f0ba72722d373ec7c27))
* **InferencingTestCase:** add ruleengine execution and expectedResults check ([c8a30fb](https://github.com/SHACL-X/shacl-x/commit/c8a30fbb759f24520e3cea81190f30792150fb60))
* **person2schema:** add missing test data triples ([d567b5d](https://github.com/SHACL-X/shacl-x/commit/d567b5d6aee82443ed7b38a650b6dea9f1b95961))
* **resources:** update rectangle.js code with new syntax for Java-JavaScript interoperability ([30605e4](https://github.com/SHACL-X/shacl-x/commit/30605e4aa8753fa664dfa9a4b98ce0fb74884452))


### General maintenance

* setting next snapshot version [skip ci] ([e2675b6](https://github.com/SHACL-X/shacl-x/commit/e2675b6093201a6a3485b081f61bc3c6e4b11b4d))

## [1.4.1](https://github.com/SHACL-X/shacl-x/compare/1.4.0...1.4.1) (2024-05-16)


### Dependency updates

* **core-deps:** update slf4j monorepo to v2.0.13 ([8e97f02](https://github.com/SHACL-X/shacl-x/commit/8e97f02e491950aa0c5af9992a0eebab67c1323c))
* **deps:** update alpine docker tag to v3.19 ([afcee5f](https://github.com/SHACL-X/shacl-x/commit/afcee5f1d0843729cafa2c6ee60fcea221177bf2))
* **deps:** update alpine docker tag to v3.19.1 ([abede59](https://github.com/SHACL-X/shacl-x/commit/abede59c4a1f31d534ac27ec287001ff8abbffbb))
* **deps:** update dependency maven to v3.8.8 ([7655383](https://github.com/SHACL-X/shacl-x/commit/765538322528ccbdeadf2f5f49db807a79591fe7))
* **deps:** update dependency maven to v3.9.6 ([999b9f1](https://github.com/SHACL-X/shacl-x/commit/999b9f1e0332b5861a5ab268fd0b607ca57c30ba))
* **deps:** update dependency maven-wrapper to v3.3.1 ([1fe0c92](https://github.com/SHACL-X/shacl-x/commit/1fe0c926d8aa5bfdce2c4189e73cd09f2bd53dcd))
* **deps:** update dependency semantic-release to v23.0.5 ([0cce181](https://github.com/SHACL-X/shacl-x/commit/0cce18198656fcc38659ac1e75ecf809120bcca3))
* **deps:** update dependency semantic-release to v23.0.6 ([3abdf47](https://github.com/SHACL-X/shacl-x/commit/3abdf47999a702505ed327536ae5ece4b8bd745d))
* **deps:** update dependency semantic-release to v23.0.8 ([9f76306](https://github.com/SHACL-X/shacl-x/commit/9f763066993c4edf009b9eed48032cd3adfdd52c))
* **deps:** update dependency semantic-release to v23.1.1 ([4833294](https://github.com/SHACL-X/shacl-x/commit/4833294271e5a93bd874b7a5bc90e055fbbf05a3))


### Build and continuous integration

* **deps:** update dependency org.apache.maven.plugins:maven-assembly-plugin to v3.7.1 ([7f81d24](https://github.com/SHACL-X/shacl-x/commit/7f81d240021ec4b320d184c2d8daeb3b3339583a))
* **deps:** update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.13.0 ([019feea](https://github.com/SHACL-X/shacl-x/commit/019feea10cf103f101442dc5b4e57b5b10455a2d))
* **deps:** update dependency org.apache.maven.plugins:maven-gpg-plugin to v3.2.4 ([e26ab99](https://github.com/SHACL-X/shacl-x/commit/e26ab996db7b73a2d013a8ecda3e613828a99d79))
* **deps:** update dependency org.apache.maven.plugins:maven-jar-plugin to v3.4.1 ([a71a93d](https://github.com/SHACL-X/shacl-x/commit/a71a93d8b39b4dea541a971adcadba1e2c11b389))
* **deps:** update dependency org.apache.maven.plugins:maven-source-plugin to v3.3.1 ([36df8d1](https://github.com/SHACL-X/shacl-x/commit/36df8d1597a672dd7a6c7d437affc1097ff7d679))
* **deps:** update docker/login-action action to v3.1.0 ([1cc6f79](https://github.com/SHACL-X/shacl-x/commit/1cc6f79ec676bf224475faf14c5def64ad2a5d83))


### General maintenance

* **readme:** update ci badge and add semantic-release one ([1328060](https://github.com/SHACL-X/shacl-x/commit/1328060c5c64ef708905da6418acacf35d763b40))
* **renovate:** disable automerge and add dockerfile deps config ([aba1a70](https://github.com/SHACL-X/shacl-x/commit/aba1a706f62cf55b3bcc61f60c49748378746f96))
* setting next snapshot version [skip ci] ([ad27552](https://github.com/SHACL-X/shacl-x/commit/ad2755205c6e2e232ff1a86fdb7abc2c8d931253))

## [1.4.0](https://github.com/SHACL-X/shacl-x/compare/1.3.2...1.4.0) (2024-03-12)


### Features

* add python version of the rdfquery library (still a bit buggy) ([7fbbd9a](https://github.com/SHACL-X/shacl-x/commit/7fbbd9ae109ae9d07b23e6c71b70a61d436ca62a))
* **py-function:** add support to PyFunction ([68909e6](https://github.com/SHACL-X/shacl-x/commit/68909e65f49914b67687dd6bccf4023ed57f2f5a))
* **script-engine:** add Python script engine ([adb77e2](https://github.com/SHACL-X/shacl-x/commit/adb77e26926123b8a68599fd7a942f5d2a3280e5))


### Dependency updates

* **core-deps:** remove not used dependency js-scriptengine ([c4aaab2](https://github.com/SHACL-X/shacl-x/commit/c4aaab2ce1114366573d042eb8a17e07e667b3ff))
* **core-deps:** update dependency graalvm to 21.1.0 ([69fd535](https://github.com/SHACL-X/shacl-x/commit/69fd53599d2dc1727ca9eebdb32cd8343f94b5e7))
* **core-deps:** update dependency org.antlr:antlr4-runtime to v4.13.1 ([df38669](https://github.com/SHACL-X/shacl-x/commit/df386695ff9d05f5047b6ae72f010910c56cdc9b))
* **core-deps:** update dependency org.apache.jena:jena-arq to v3.17.0 ([590472e](https://github.com/SHACL-X/shacl-x/commit/590472ee30e4f3cf8ab2c35b9ff5f4edb46d8dd0))
* **core-deps:** update dependency org.graalvm.js:js to v21.3.8 ([8d1b6e8](https://github.com/SHACL-X/shacl-x/commit/8d1b6e8884ab7781ef949c88ae8441b73f82f7ce))
* **core-deps:** update dependency org.graalvm.js:js to v22 ([e5f87ef](https://github.com/SHACL-X/shacl-x/commit/e5f87efb12e8f1c25a517807a48bf0fad4817a0b))
* **core-deps:** update dependency org.graalvm.polyglot:js to v23.1.1 ([338047c](https://github.com/SHACL-X/shacl-x/commit/338047c6c8c23f126454c4ff55e915a55bf793e5))
* **core-deps:** update dependency org.graalvm.polyglot:js to v23.1.2 ([b706ca1](https://github.com/SHACL-X/shacl-x/commit/b706ca1d6b51dbc8bff20c5055c8abef941d666c))
* **core-deps:** update dependency org.graalvm.polyglot:polyglot and :js to v23.1.0 ([#21](https://github.com/SHACL-X/shacl-x/issues/21)) ([673d1f1](https://github.com/SHACL-X/shacl-x/commit/673d1f106adda59e02d3823430b16cbc68ef8455))
* **core-deps:** update dependency org.graalvm.polyglot:polyglot to v23.1.1 ([4599ce8](https://github.com/SHACL-X/shacl-x/commit/4599ce81a0c96ea9fa499d11f9cf988efb848f71))
* **core-deps:** update dependency org.graalvm.polyglot:polyglot to v23.1.2 ([f14a8b3](https://github.com/SHACL-X/shacl-x/commit/f14a8b383eb734f2577caf4bd6cf8ba22e5f7acb))
* **core-deps:** update slf4j monorepo to v2.0.12 ([28a6d4b](https://github.com/SHACL-X/shacl-x/commit/28a6d4b546c768365f366f776620379a582d469e))
* **core-deps:** update ver.slf4j to v1.7.36 ([b65bdfc](https://github.com/SHACL-X/shacl-x/commit/b65bdfc7f0569d45a8c81055866e3cf112fee1b4))
* **core-deps:** update ver.slf4j to v2 ([1fcb57f](https://github.com/SHACL-X/shacl-x/commit/1fcb57f1b41e4ca5d30055e5e89c3cc64522d077))
* **core-deps:** update ver.slf4j to v2.0.10 ([9ccecc9](https://github.com/SHACL-X/shacl-x/commit/9ccecc9c676c4e4ab3361b7d3e899d5f543ff7e0))
* **core-deps:** update ver.slf4j to v2.0.11 ([57c3462](https://github.com/SHACL-X/shacl-x/commit/57c3462b2a62e2430eea4990828114c2cee82733))
* **deps:** add dependency semantic-release-preconfigured-conventional-commits ([2624a94](https://github.com/SHACL-X/shacl-x/commit/2624a94b424dce981da1b4021ecf1c58b542736d))
* **deps:** rename slf4j-log4j12 to slf4j-reload4j because the artifact was relocated ([f505e67](https://github.com/SHACL-X/shacl-x/commit/f505e678960bb0e11c61635fcedadcbea38e7c4e))


### Bug Fixes

* change the rdf_query function call in PyGraph with the right one ([a69b72d](https://github.com/SHACL-X/shacl-x/commit/a69b72df308f95a1c095359b5a14fbe00d2b9b70))
* **py-rdfquery:** add null checks in match query next solution function ([5b3066e](https://github.com/SHACL-X/shacl-x/commit/5b3066e2d86f670bc955709ac0c960e82c51aaaa))
* **py-rdfquery:** walk_path conditions and minor changes ([dc5c56c](https://github.com/SHACL-X/shacl-x/commit/dc5c56c5a716067ada9580d979f8c9defc264709))
* **py-script-engine:** update "get_args" function to return an empty array instead of a 1-element array with an empty string when the function has zero params ([6f0cd01](https://github.com/SHACL-X/shacl-x/commit/6f0cd0134f226b627c7ce0cf9ec9a53e2774cecc))
* **shacl-model:** replace interface implemented by SHPyFunction with the right one (SHJSExecutable -> SHPyExecutable) ([8a1497f](https://github.com/SHACL-X/shacl-x/commit/8a1497f29ce32ac9dd1afdd47b40205e8be7323b))
* solve bugs in rdfquery python library ([823baab](https://github.com/SHACL-X/shacl-x/commit/823baab32dae06efc5b86449cb42fadf068cdb42))
* **validation:** preferred constraint executor bug ([d63504c](https://github.com/SHACL-X/shacl-x/commit/d63504c8a2ac04eaa949dfd9a368717e4447c18a))
* **vocabulary:** update py_data and _shapes variable name because the "$" was breaking the Python code ([f2aeb58](https://github.com/SHACL-X/shacl-x/commit/f2aeb58230d96c7ded8ec44e63556913dc7511d2))


### Documentation

* fix typo in TestCaseType ([0920a8d](https://github.com/SHACL-X/shacl-x/commit/0920a8db1621db1bb3f91e4a4e605ca46ee1264e))


### Tests

* add test cases, types, and context for testing py-based validation/rules ([8637b6d](https://github.com/SHACL-X/shacl-x/commit/8637b6d4a8c36112697103cb2c5be78f08bc4bec))
* **deps:** update dependency junit:junit to v4.13.2 ([79a29a4](https://github.com/SHACL-X/shacl-x/commit/79a29a44544f4ac0e392ac05a10ee376de2833a2))
* fix tests-rdfquery paths with right URI ("py" instead of "js") ([7f1aef5](https://github.com/SHACL-X/shacl-x/commit/7f1aef514e258a6fe50aa3ba125bfcdc12f34387))
* **function:** add Python test case context ([130929a](https://github.com/SHACL-X/shacl-x/commit/130929ab2424655d7effe06be2c822037361eb6a))
* **PropertyShape:** add py-based property validator (sh:PyValidator) ([6d2652a](https://github.com/SHACL-X/shacl-x/commit/6d2652a18238bd643eb76132f4b9bced2fcbe7cc))
* **py-dash:** add max_length constraint test ([c4c7d59](https://github.com/SHACL-X/shacl-x/commit/c4c7d59a5a31841f8d1a2bcb4c59bead834dbbe9))
* **py-dash:** add max_length_component implementation ([bb9e8bb](https://github.com/SHACL-X/shacl-x/commit/bb9e8bb08b7df1e5b5be6680800084a6f3ec2a94))
* **py-function:** add simplePyFunctions tests ([4988554](https://github.com/SHACL-X/shacl-x/commit/49885548695dbf9303162021287ce00ef12633fb))
* **py-rdfquery:** add paths tests ([b5e45d9](https://github.com/SHACL-X/shacl-x/commit/b5e45d918e6e7b750bd406ec5ad44b2713f3b70d))
* **py-rdfquery:** add predicate_path_single test ([bf8241e](https://github.com/SHACL-X/shacl-x/commit/bf8241e5782a0fc8bb34d509862bda3723c2a736))
* **script-engine:** add test for PyTermFactory execution within the guest language (Python) using the polyglot api ([bf5fff5](https://github.com/SHACL-X/shacl-x/commit/bf5fff56cf62a9b262fc78096bf02d229913d660))
* **shacl-js:** rewrite JS tests using ECMAScript >= 6 ([359e7a5](https://github.com/SHACL-X/shacl-x/commit/359e7a559d1f29930e8c93afc8eda64229d1f6cd))
* **validation:** add germanLabel PyConstraint test case ([5396ff2](https://github.com/SHACL-X/shacl-x/commit/5396ff2015b8a05c68cb96864a7d07f487003b53))
* **validation:** add simple PyConstraint test case ([614ea9a](https://github.com/SHACL-X/shacl-x/commit/614ea9a902db80504e1331f4ededbf2f38371605))


### Build and continuous integration

* add jdk setup step and github_username env var ([322abff](https://github.com/SHACL-X/shacl-x/commit/322abff651be018af1bb49f3f640466c952f6572))
* add job to run maven tests ([7677657](https://github.com/SHACL-X/shacl-x/commit/7677657b055d0691ce5634f51e585dd78d222ea7))
* add maven build config ([b03f3e9](https://github.com/SHACL-X/shacl-x/commit/b03f3e92efcd17e7fa82d84626645a2c8fb8bcdc))
* add maven wrapper ([d11cb5c](https://github.com/SHACL-X/shacl-x/commit/d11cb5c31659b9fd62356377908dfefaf2607f80))
* **deps:** update actions/checkout action to v4 ([9496758](https://github.com/SHACL-X/shacl-x/commit/9496758eec57be7caf842c6ece32eb5b22927118))
* **deps:** update actions/setup-java action to v4 ([0777d70](https://github.com/SHACL-X/shacl-x/commit/0777d70a6c336676c5eebd2259171d7134dd23bc))
* **deps:** update dependency org.apache.maven.plugins:maven-assembly-plugin to v3.6.0 ([ef2138f](https://github.com/SHACL-X/shacl-x/commit/ef2138fc97b91bbdce7609df50194a1466bd2050))
* **deps:** update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.11.0 ([cc1de18](https://github.com/SHACL-X/shacl-x/commit/cc1de18a886c37a00cd5d60e641947fc92fc7736))
* **deps:** update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.12.0 ([1598b07](https://github.com/SHACL-X/shacl-x/commit/1598b07fb9c688075d37b9c778e5aa4cb9554265))
* **deps:** update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.12.1 ([833f7c4](https://github.com/SHACL-X/shacl-x/commit/833f7c478992b1f72f0fc59aa7d111c69dde2c0d))
* **deps:** update dependency org.apache.maven.plugins:maven-compiler-plugin to v3.8.1 ([4e4f554](https://github.com/SHACL-X/shacl-x/commit/4e4f5546f5f6996d0f70b5027d0896abe4aa3ac5))
* **deps:** update dependency org.apache.maven.plugins:maven-gpg-plugin to v3 ([c0fe4e5](https://github.com/SHACL-X/shacl-x/commit/c0fe4e5f67f0189c10fc31f13bf4f5fbec1009ab))
* **deps:** update dependency org.apache.maven.plugins:maven-jar-plugin to v3.1.2 ([7bbb2bb](https://github.com/SHACL-X/shacl-x/commit/7bbb2bb673a62ef12ca334c19f1fa1c2e02b6cf9))
* **deps:** update dependency org.apache.maven.plugins:maven-jar-plugin to v3.3.0 ([4a3dd89](https://github.com/SHACL-X/shacl-x/commit/4a3dd894e81cbe7072dfc3faff7eb126c9ce7150))
* **deps:** update dependency org.apache.maven.plugins:maven-javadoc-plugin to v2.10.4 ([2397703](https://github.com/SHACL-X/shacl-x/commit/23977036091c38084297a6103faf66af84fad5ff))
* **deps:** update dependency org.apache.maven.plugins:maven-javadoc-plugin to v3 ([5f5e442](https://github.com/SHACL-X/shacl-x/commit/5f5e44220924fb8471d140e28417d0435a5aad83))
* **deps:** update dependency org.apache.maven.plugins:maven-release-plugin to v3 ([c29d6b9](https://github.com/SHACL-X/shacl-x/commit/c29d6b9336e8e31c17dc4876d5fccfb60bf237bf))
* **deps:** update dependency org.apache.maven.plugins:maven-source-plugin to v3.3.0 ([7aa3847](https://github.com/SHACL-X/shacl-x/commit/7aa3847e644458d03fde8a85cdb6b8bf156103e3))
* **deps:** update dependency org.sonatype.plugins:nexus-staging-maven-plugin to v1.6.13 ([da2b0e3](https://github.com/SHACL-X/shacl-x/commit/da2b0e3294eef88e078ddb4c21e24220af8ee5bd))
* replace config with sem-release one ([16fc5be](https://github.com/SHACL-X/shacl-x/commit/16fc5be67c228dcdb5b7305d4ca5c1f8c2fc01e9))
* update config ([66ea3b6](https://github.com/SHACL-X/shacl-x/commit/66ea3b63b35e19b2c4c5ecd9e1f05c7046b77cf9))


### General maintenance

* add Dockerfile ([c075687](https://github.com/SHACL-X/shacl-x/commit/c075687e97b238db09aae0ca31e43eee525ab709))
* add GH maven registry sem-release config ([0e85a54](https://github.com/SHACL-X/shacl-x/commit/0e85a5487e32ea4973466c41836c40fb1bfc60ed))
* Configure Renovate ([#2](https://github.com/SHACL-X/shacl-x/issues/2)) ([4701ea6](https://github.com/SHACL-X/shacl-x/commit/4701ea6971c19114ff1c3101414477860bf6ef80))
* **gitignore:** add node_modules dir ([f4ffb04](https://github.com/SHACL-X/shacl-x/commit/f4ffb0440f667014a0848ed669863418abab5a51))
* **jena-prefixes:** replace old prefixes with new ones ([e32cd28](https://github.com/SHACL-X/shacl-x/commit/e32cd28f87a3037c1224ea7af7850b175bdf3e3e))
* **jena-prefixes:** replace old prefixes with new ones as explained at https://jena.apache.org/documentation/query/library-propfunc.html ([a366532](https://github.com/SHACL-X/shacl-x/commit/a366532248ba610dfc7599df90bf7cd42773ab21))
* **model:** add implementation of python-based constraints ([302d0db](https://github.com/SHACL-X/shacl-x/commit/302d0db49aad566bdb8da4dd9dba0983ab6c9562))
* **model:** add terms to be used by Python ([b7b4200](https://github.com/SHACL-X/shacl-x/commit/b7b4200bb555f71df3f081826a2afa31bfad6e1d))
* **model:** extend shacl model to support python-based constraints and rules ([eeffeaa](https://github.com/SHACL-X/shacl-x/commit/eeffeaad4e7d0a70bb08fde257ee31043c83bbf1))
* **py-script-engine:** add new function to read function parameters ([7294660](https://github.com/SHACL-X/shacl-x/commit/729466021381ff8857cb8e3dbac8d0e50617dd09))
* **readme:** add CI badge ([8ae9197](https://github.com/SHACL-X/shacl-x/commit/8ae9197f3c0d0744535b099f82cc3900f3dfa747))
* **readme:** update with information about the fork ([b0439ec](https://github.com/SHACL-X/shacl-x/commit/b0439ec2f36fe1537265c60fecbacbb916250a3f))
* replace dash.js and rdfquery.js, where needed, with python versions (yet they aren't implemented) ([19a7422](https://github.com/SHACL-X/shacl-x/commit/19a7422b368c7f01df0f4a6e5eb3bf5c355ce869))
* replace project information with shacl-x related ones ([6efc4d8](https://github.com/SHACL-X/shacl-x/commit/6efc4d88dc8317309b0bd161ed8a67366a22541e))
* **rules:** add python as rule language and python-based rules components ([01c6cf6](https://github.com/SHACL-X/shacl-x/commit/01c6cf62604ec319b59867b25a93d4d0e13dc810))
* **script-engine:** disable nashorn-compat mode ([920f709](https://github.com/SHACL-X/shacl-x/commit/920f7094cd2860bcd87536902ec378171a07d8af))
* **script-engine:** remove nashorn ([302fddd](https://github.com/SHACL-X/shacl-x/commit/302fdddfa041a01f0f3bf4e2e9e926ff27e57cfd))
* **sem-release:** add docker release config ([be8d1fc](https://github.com/SHACL-X/shacl-x/commit/be8d1fc903d7e2742e9f26181662171ab7db9b08))
* **shacl-js:** add support for ECMAScript 12 (2021) ([34ae927](https://github.com/SHACL-X/shacl-x/commit/34ae9275944c575189f75995da505d21cb1382a7))
* **shacl-vocabulary:** add shacl-py terms ([3f33f94](https://github.com/SHACL-X/shacl-x/commit/3f33f94a6d184e5a4c914b6c28c72b039cbf7061))
* update method calls in JavaScript to comply with Graal Polyglot programming syntax ([b2b7688](https://github.com/SHACL-X/shacl-x/commit/b2b7688891205cb91d25dd01af8efb743581a614))
* **validation:** add components to support py-based validation ([4726024](https://github.com/SHACL-X/shacl-x/commit/4726024b2c2613b274e918e670ed8bd764026d13))
* **vocabulary:** add PyTestCase to the DASH vocabulary ([dac670e](https://github.com/SHACL-X/shacl-x/commit/dac670e0ede78d8081d2219c50a44759eeab7080))
* **vocabulary:** add Python components to SHACL and DASH vocabularies ([74bdbcb](https://github.com/SHACL-X/shacl-x/commit/74bdbcbfbad26a95447d4173e3c725e7e048bf86))
* **vocabulary:** add RDF Query Python library to DASH ([47f2a65](https://github.com/SHACL-X/shacl-x/commit/47f2a65d310f7ee3a89b701f8c22277a15b08940))


### Refactoring

* clean dash test cases ([d680023](https://github.com/SHACL-X/shacl-x/commit/d680023852cba86aefa5ca48518ae2b47152748e))
* **js:** replace 'var'(s) with 'let'(s) ([bcf536f](https://github.com/SHACL-X/shacl-x/commit/bcf536f81ef66afcff57c6245a32e7fa6f205df4))
* remove useless old Eclipse settings ([687293a](https://github.com/SHACL-X/shacl-x/commit/687293a2c1fc1d3320ff7e7d618f123c4a9b610c))
* **script-engine:** replace interfaces for JS and Py script engine with more general ScriptEngine ([97de168](https://github.com/SHACL-X/shacl-x/commit/97de168af4e92e7eb030be0892ccfa5e93d2c121))
