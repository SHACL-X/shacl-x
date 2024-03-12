[![Autorelease](https://github.com/SHACL-X/shacl-x/actions/workflows/autorelease.yml/badge.svg?branch=master)](https://github.com/SHACL-X/shacl-x/actions/workflows/autorelease.yml)
[![semantic-release: angular](https://img.shields.io/badge/semantic--release-angular-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)


# TopBraid SHACL API Extended

**This is a fork of the [TopBraid SHACL API](https://github.com/TopQuadrant/shacl), an open source implementation of the W3C Shapes Constraint Language (SHACL) based on Apache Jena.**

Starting from [SHACL API v1.4.0](https://github.com/TopQuadrant/shacl/releases/tag/v1.4.0), the SHACL-JS support was removed to avoid Nashorn issues.

This fork brings the SHACL JavaScript Extensions (SHACL-JS) features back by leveraging GraalVM and its Polyglot APIs. Not only the SHACL-JS features are brought back but new extensions are introduced, such as the SHACL Python Extensions (SHACL-Py).


Coverage:
* [SHACL Core and SHACL-SPARQL validation](https://www.w3.org/TR/shacl/)
* [SHACL Advanced Features (Rules etc)](https://www.w3.org/TR/shacl-af/)
* [SHACL Compact Syntax](https://w3c.github.io/shacl/shacl-compact-syntax/)
* [SHACL JavaScript Extensions](https://www.w3.org/TR/shacl-js/)
* SHACL Python Extensions

Contact: Ashley Caselli ([ashley.caselli@unige.ch](mailto:ashley.caselli@unige.ch))
