// Functions implementing the validators of SHACL-JS
// Also include validators for the constraint components of the DASH namespace

// Also included: implementations of the standard DASH functions

// There is no validator for sh:property as this is expected to be
// natively implemented by the surrounding engine.

let XSDIntegerTypes = new NodeSet();
XSDIntegerTypes.add(T("xsd:integer"));

let XSDDecimalTypes = new NodeSet();
XSDDecimalTypes.addAll(XSDIntegerTypes.toArray());
XSDDecimalTypes.add(T("xsd:decimal"));
XSDDecimalTypes.add(T("xsd:float"));

function validateAnd($value, $and) {
	let shapes = new RDFQueryUtil($shapes).rdfListToArray($and);
	for(let i = 0; i < shapes.length; i++) {
		if(!SHACL.nodeConformsToShape($value, shapes[i])) {
			return false;
		}
	}
	return true;
}

function validateClass($value, $class) {
	return new RDFQueryUtil($data).isInstanceOf($value, $class);
}

function validateClosed($value, $closed, $ignoredProperties, $currentShape) {
	if(!T("true").equals($closed)) {
		return;
	}
	let allowed = $shapes.query().
		match($currentShape, "sh:property", "?propertyShape").
		match("?propertyShape", "sh:path", "?path").
		filter(function(solution) { return solution.path.isURI() } ).
		getNodeSet("?path");
	if($ignoredProperties) {
		allowed.addAll(new RDFQueryUtil($shapes).rdfListToArray($ignoredProperties));
	}
	let results = [];
	$data.query().
		match($value, "?predicate", "?object").
		filter(function(sol) { return !allowed.contains(sol.predicate)}).
		forEach(function(sol) { 
			results.push({ 
				path : sol.predicate,
				value : sol.object
			});
		});
	return results;
}

function validateClosedByTypesNode($this, $closedByTypes) {
	if(!T("true").equals($closedByTypes)) {
		return;
	}
	let results = [];
	let allowedProperties = new NodeSet();
	$data.query().
		match($this, "rdf:type", "?directType").
		path("?directType", { zeroOrMore : T("rdfs:subClassOf") }, "?type").
		forEachNode("?type", function(type) {
			$shapes.query().
				match(type, "sh:property", "?pshape").
				match("?pshape", "sh:path", "?path").
				filter(function(sol) { return sol.path.isURI() }).
				addAllNodes("?path", allowedProperties);
		});
	$data.query().
		match($this, "?predicate", "?object").
		filter(function(sol) { return !T("rdf:type").equals(sol.predicate) }).
		filter(function(sol) { return !allowedProperties.contains(sol.predicate) }).
		forEach(function(sol) {
			results.push({
				path: sol.predicate,
				value: sol.object
			});
		})
	return results;
}

function validateCoExistsWith($this, $path, $coExistsWith) {
	let path = toRDFQueryPath($path);
	let has1 = $data.query().path($this, path, null).getCount() > 0;
	let has2 = $data.query().match($this, $coExistsWith, null).getCount() > 0;
	return has1 == has2;
}

function validateDatatype($value, $datatype) {
	if($value.isLiteral()) {
		return $datatype.equals($value.datatype) && isValidForDatatype($value.lex, $datatype);
	}
	else {
		return false;
	}
}

function validateDisjoint($this, $value, $disjoint) {
	return !$data.query().match($this, $disjoint, $value).hasSolution();
}

function validateEqualsProperty($this, $path, $equals) {
	let results = [];
	let path = toRDFQueryPath($path);
	$data.query().path($this, path, "?value").forEach(
		function(solution) {
			if(!$data.query().match($this, $equals, solution.value).hasSolution()) {
				results.push({
					value: solution.value
				});
			}
		});
	$data.query().match($this, $equals, "?value").forEach(
		function(solution) {
			if(!$data.query().path($this, path, solution.value).hasSolution()) {
				results.push({
					value: solution.value
				});
			}
		});
	return results;
}

let validateEqualsNode = function ($this, $equals) {
    let results = [];
    let solutions = 0;
    $data.query().path($this, $equals, "?value").forEach(
        function (solution) {
            solutions++;
            if (SHACL.compareNodes($this, solution['value']) !== 0) {
                results.push({
                    value: solution.value
                });
            }
        });
    if (results.length === 0 && solutions === 0) {
        results.push({
            value: $this
        });
    }
    return results;
};

function validateHasValueNode($this, $hasValue) {
	return $this.equals($hasValue);
}

function validateHasValueProperty($this, $path, $hasValue) {
	let count = $data.query().path($this, toRDFQueryPath($path), $hasValue).getCount();
	return count > 0;
}

function validateHasValueWithClass($this, $path, $hasValueWithClass) {
	return $data.query().
			path($this, toRDFQueryPath($path), "?value").
			filter(function(sol) { return new RDFQueryUtil($data).isInstanceOf(sol.value, $hasValueWithClass) }).
			hasSolution();
}

function validateIn($value, $in) {
	let set = new NodeSet();
	set.addAll(new RDFQueryUtil($shapes).rdfListToArray($in));
	return set.contains($value);
}

function validateLanguageIn($value, $languageIn) {
	if(!$value.isLiteral()) {
		return false;
	}
	let lang = $value.language;
	if(!lang || lang === "") {
		return false;
	}
	let ls = new RDFQueryUtil($shapes).rdfListToArray($languageIn);
	for(let i = 0; i < ls.length; i++) {
		if(lang.startsWith(ls[i].lex)) {
			return true;
		}
	}
	return false;
}

function validateLessThanProperty($this, $path, $lessThan) {
	let results = [];
	$data.query().
		path($this, toRDFQueryPath($path), "?value").
		match($this, $lessThan, "?otherValue").
		forEach(function(sol) {
					let c = SHACL.compareNodes(sol.value, sol.otherValue);
					if(c == null || c >= 0) {
						results.push({
							value: sol.value
						});
					}
				});
	return results;
}

function validateLessThanOrEqualsProperty($this, $path, $lessThanOrEquals) {
	let results = [];
	$data.query().
		path($this, toRDFQueryPath($path), "?value").
		match($this, $lessThanOrEquals, "?otherValue").
		forEach(function(sol) {
					let c = SHACL.compareNodes(sol.value, sol.otherValue);
					if(c == null || c > 0) {
						results.push({
							value: sol.value
						});
					}
				});
	return results;
}

function validateMaxCountProperty($this, $path, $maxCount) {
	let count = $data.query().path($this, toRDFQueryPath($path), "?any").getCount();
	return count <= Number($maxCount.value);
}

function validateMaxExclusive($value, $maxExclusive) {
	return $value.isLiteral() && Number($value.lex) < Number($maxExclusive.lex);
}

function validateMaxInclusive($value, $maxInclusive) {
	return $value.isLiteral() && Number($value.lex) <= Number($maxInclusive.lex);
}

function validateMaxLength($value, $maxLength) {
	if($value.isBlankNode()) {
		return false;
	}
	return $value.value.length <= Number($maxLength.lex);
}

function validateMinCountProperty($this, $path, $minCount) {
	let count = $data.query().path($this, toRDFQueryPath($path), "?any").getCount();
	return count >= Number($minCount.value);
}

function validateMinExclusive($value, $minExclusive) {
	return $value.isLiteral() && Number($value.lex) > Number($minExclusive.lex);
}

function validateMinInclusive($value, $minInclusive) {
	return $value.isLiteral() && Number($value.lex) >= Number($minInclusive.lex);
}

function validateMinLength($value, $minLength) {
	if($value.isBlankNode()) {
		return false;
	}
	return $value.value.length >= Number($minLength.lex);
}

function validateNodeKind($value, $nodeKind) {
	if($value.isBlankNode()) {
		return T("sh:BlankNode").equals($nodeKind) || 
			T("sh:BlankNodeOrIRI").equals($nodeKind) ||
			T("sh:BlankNodeOrLiteral").equals($nodeKind);
	}
	else if($value.isURI()) {
		return T("sh:IRI").equals($nodeKind) || 
			T("sh:BlankNodeOrIRI").equals($nodeKind) ||
			T("sh:IRIOrLiteral").equals($nodeKind);
	}
	else if($value.isLiteral()) {
		return T("sh:Literal").equals($nodeKind) || 
			T("sh:BlankNodeOrLiteral").equals($nodeKind) ||
			T("sh:IRIOrLiteral").equals($nodeKind);
	}
}

function validateNode($value, $node) {
	return SHACL.nodeConformsToShape($value, $node);
}

function validateNonRecursiveProperty($this, $path, $nonRecursive) {
	if(T("true").equals($nonRecursive)) {
		if($data.query().path($this, toRDFQueryPath($path), $this).hasSolution()) {
			return {
				path: $path,
				value: $this
			}
		}
	}
}

function validateNot($value, $not) {
	return !SHACL.nodeConformsToShape($value, $not);
}

function validateOr($value, $or) {
	let shapes = new RDFQueryUtil($shapes).rdfListToArray($or);
	for(let i = 0; i < shapes.length; i++) {
		if(SHACL.nodeConformsToShape($value, shapes[i])) {
			return true;
		}
	}
	return false;
}

function validatePattern($value, $pattern, $flags) {
	if($value.isBlankNode()) {
		return false;
	}
	let re = $flags ? new RegExp($pattern.lex, $flags.lex) : new RegExp($pattern.lex);
	return re.test($value.value);
}

function validatePrimaryKeyProperty($this, $path, $uriStart) {
	if(!$this.isURI()) {
		return "Must be an IRI";
	}
	if($data.query().path($this, toRDFQueryPath($path), null).getCount() != 1) {
		return "Must have exactly one value";
	}
	let value = $data.query().path($this, toRDFQueryPath($path), "?value").getNode("?value");
	let uri = $uriStart.lex + encodeURIComponent(value.value);
	if(!$this.uri.equals(uri)) {
		return "Does not have URI " + uri;
	}
}

function validateQualifiedMaxCountProperty($this, $path, $qualifiedValueShape, $qualifiedValueShapesDisjoint, $qualifiedMaxCount, $currentShape) {
	let c = validateQualifiedHelper($this, $path, $qualifiedValueShape, $qualifiedValueShapesDisjoint, $currentShape);
	return c <= Number($qualifiedMaxCount.lex);
}

function validateQualifiedMinCountProperty($this, $path, $qualifiedValueShape, $qualifiedValueShapesDisjoint, $qualifiedMinCount, $currentShape) {
	let c = validateQualifiedHelper($this, $path, $qualifiedValueShape, $qualifiedValueShapesDisjoint, $currentShape);
	return c >= Number($qualifiedMinCount.lex);
}

function validateQualifiedHelper($this, $path, $qualifiedValueShape, $qualifiedValueShapesDisjoint, $currentShape) {
	let siblingShapes = new NodeSet();
	if(T("true").equals($qualifiedValueShapesDisjoint)) {
		$shapes.query().
			match("?parentShape", "sh:property", $currentShape).
			match("?parentShape", "sh:property", "?sibling").
			match("?sibling", "sh:qualifiedValueShape", "?siblingShape").
			filter(exprNotEquals("?siblingShape", $qualifiedValueShape)) .
			addAllNodes("?siblingShape", siblingShapes);
	}
	return $data.query().
		path($this, toRDFQueryPath($path), "?value").
		filter(function(sol) { 
			return SHACL.nodeConformsToShape(sol.value, $qualifiedValueShape) &&
				!validateQualifiedConformsToASibling(sol.value, siblingShapes.toArray()); 
		}).
		getCount();
}

function validateQualifiedConformsToASibling(value, siblingShapes) {
	for(let i = 0; i < siblingShapes.length; i++) {
		if(SHACL.nodeConformsToShape(value, siblingShapes[i])) {
			return true;
		}
	}
	return false;
}

function validateRootClass($value, $rootClass) {
	return $data.query().path($value, { zeroOrMore: T("rdfs:subClassOf") }, $rootClass).hasSolution();
}

function validateSingleLine($value, $singleLine) {
	if($singleLine.lex == 'true' && $value.isLiteral() && ($value.lex.indexOf('\n') >= 0 || $value.lex.indexOf('\r') >= 0)) {
		return false;
	}
	else {
		return true;
	}
}

function validateStem($value, $stem) {
	return $value.isURI() && $value.uri.startsWith($stem.lex);
}

function validateSubSetOf($this, $subSetOf, $value) {
	return $data.query().match($this, $subSetOf, $value).hasSolution();
}

function validateSymmetric($this, $path, $symmetric) {
	let results = [];
	if(T("true").equals($symmetric)) {
		let path = toRDFQueryPath($path);
		$data.query().
			path($this, path, "?value").
			filter(function(sol) {
				return !$data.query().path(sol.value, path, $this).hasSolution();
			}).
			forEach(function(sol) {
				results.push({
					value: sol.value
				})
			});
	}
	return results;
}

function validateUniqueLangProperty($this, $uniqueLang, $path) {
	if(!T("true").equals($uniqueLang)) {
		return;
	}
	let map = {};
	$data.query().path($this, toRDFQueryPath($path), "?value").forEach(function(sol) {
		let lang = sol.value.language;
		if(lang && lang != "") {
			let old = map[lang];
			if(!old) {
				map[lang] = 1;
			}
			else {
				map[lang] = old + 1;
			}
		}
	})
	let results = [];
	for(let lang in map) {
		if(map.hasOwnProperty(lang)) {
			let count = map[lang];
			if(count > 1) {
				results.push("Language \"" + lang + "\" has been used by " + count + " values");
			}
		}
	}
	return results;
}

function validateUniqueValueForClass($this, $uniqueValueForClass, $path) {
	let results = [];
	$data.query().
		path($this, toRDFQueryPath($path), "?value").
		path("?other", toRDFQueryPath($path), "?value").
		filter(function(sol) {
				return !$this.equals(sol.other);
			}).
		filter(function(sol) {
				return new RDFQueryUtil($data).isInstanceOf(sol.other, $uniqueValueForClass)
			}).
		forEach(function(sol) {
			results.push({
				other: sol.other,
				value: sol.value
			})
		});
	return results;
}

function validateXone($value, $xone) {
	let shapes = new RDFQueryUtil($shapes).rdfListToArray($xone);
	let count = 0;
	for(let i = 0; i < shapes.length; i++) {
		if(SHACL.nodeConformsToShape($value, shapes[i])) {
			count++;
		}
	}
	return count == 1;
}


// DASH functions -------------------------------------------------------------

// dash:toString
function dash_toString($arg) {
	if($arg.isLiteral()) {
		return NodeFactory.literal($arg.lex, T("xsd:string"));
	}
	else if($arg.isURI()) {
		return NodeFactory.literal($arg.uri, T("xsd:string"));
	}
	else {
		return null;
	}
}


// DASH targets ---------------------------------------------------------------

// dash:AllObjectsTarget
function dash_allObjects() {
	return $data.query().match(null, null, "?object").getNodeSet("?object").toArray();
}

// dash:AllSubjectsTarget
function dash_allSubjects() {
	return $data.query().match("?subject", null, null).getNodeSet("?subject").toArray();
}


// Utilities ------------------------------------------------------------------

function toRDFQueryPath(shPath) {
    if (shPath.termType === "Collection") {
        let paths = new RDFQueryUtil($shapes).rdfListToArray(shPath);
        let result = [];
        for (let i = 0; i < paths.length; i++) {
            result.push(toRDFQueryPath(paths[i]));
        }
        return result;
    }
	if(shPath.isURI()) {
		return shPath;
	}
	else if(shPath.isBlankNode()) {
		let util = new RDFQueryUtil($shapes);
		if($shapes.query().getObject(shPath, "rdf:first")) {
			let paths = util.rdfListToArray(shPath);
			let result = [];
			for(let i = 0; i < paths.length; i++) {
				result.push(toRDFQueryPath(paths[i]));
			}
			return result;
		}
		let alternativePath = $shapes.query().getObject(shPath, "sh:alternativePath");
		if(alternativePath) {
			let paths = util.rdfListToArray(alternativePath);
			let result = [];
			for(let i = 0; i < paths.length; i++) {
				result.push(toRDFQueryPath(paths[i]));
			}
			return { or : result };
		}
		let zeroOrMorePath = $shapes.query().getObject(shPath, "sh:zeroOrMorePath");
		if(zeroOrMorePath) {
			return { zeroOrMore : toRDFQueryPath(zeroOrMorePath) };
		}
		let oneOrMorePath = $shapes.query().getObject(shPath, "sh:oneOrMorePath");
		if(oneOrMorePath) {
			return { oneOrMore : toRDFQueryPath(oneOrMorePath) };
		}
		let zeroOrOnePath = $shapes.query().getObject(shPath, "sh:zeroOrOnePath");
		if(zeroOrOnePath) {
			return { zeroOrOne : toRDFQueryPath(zeroOrOnePath) };
		}
		let inversePath = $shapes.query().getObject(shPath, "sh:inversePath");
		if(inversePath) {
			return { inverse : toRDFQueryPath(inversePath) };
		}
	}
	throw "Unsupported SHACL path " + shPath;
	// TODO: implement conforming to AbstractQuery.path syntax
	return shPath;
}


// Private helper functions

//TODO: Support more datatypes
function isValidForDatatype(lex, datatype) {
	if(XSDIntegerTypes.contains(datatype)) {
		let r = parseInt(lex);
		return !isNaN(r);
	}
	else if(XSDDecimalTypes.contains(datatype)) {
		let r = parseFloat(lex);
		return !isNaN(r);
	}
	else if (datatype.value === "http://www.w3.org/2001/XMLSchema#boolean") {
        return lex === "true" || lex === "false";
    }
	else {
		return true;
	}
}

function RDFQueryUtil($source) {
	this.source = $source;
}

RDFQueryUtil.prototype.getInstancesOf = function($class) {
	let set = new NodeSet();
	let classes = this.getSubClassesOf($class);
	classes.add($class);
	let car = classes.toArray();
	for(let i = 0; i < car.length; i++) {
		set.addAll(RDFQuery(this.source).match("?instance", "rdf:type", car[i]).getNodeArray("?instance"));
	}
	return set;
}

RDFQueryUtil.prototype.getObject = function($subject, $predicate) {
	if(!$subject) {
		throw "Missing subject";
	}
	if(!$predicate) {
		throw "Missing predicate";
	}
	return RDFQuery(this.source).match($subject, $predicate, "?object").getNode("?object");
}

RDFQueryUtil.prototype.getSubClassesOf = function($class) {
	let set = new NodeSet();
	this.walkSubjects(set, $class, T("rdfs:subClassOf"));
	return set;
}

RDFQueryUtil.prototype.isInstanceOf = function($instance, $class) {
	let classes = this.getSubClassesOf($class);
	let types = $data.query().match($instance, "rdf:type", "?type");
	for(let n = types.nextSolution(); n; n = types.nextSolution()) {
		if(n.type.equals($class) || classes.contains(n.type)) {
			types.close();
			return true;
		}
	}
	return false;
}

RDFQueryUtil.prototype.rdfListToArray = function($rdfList) {
    if ($rdfList.elements != null) {
        return $rdfList.elements;
    } else {
        let array = [];
        while (!T("rdf:nil").equals($rdfList)) {
            array.push(this.getObject($rdfList, T("rdf:first")));
            $rdfList = this.getObject($rdfList, T("rdf:rest"));
        }
        return array;
    }
}

RDFQueryUtil.prototype.walkObjects = function($results, $subject, $predicate) {
	let it = this.source.find($subject, $predicate, null);
	for(let n = it.next(); n; n = it.next()) {
		if(!$results.contains(n.object)) {
			$results.add(n.object);
			this.walkObjects($results, n.object, $predicate);
		}
	}
}

RDFQueryUtil.prototype.walkSubjects = function($results, $object, $predicate) {
	let it = this.source.find(null, $predicate, $object);
	for(let n = it.next(); n; n = it.next()) {
		if(!$results.contains(n.subject)) {
			$results.add(n.subject);
			this.walkSubjects($results, n.subject, $predicate);
		}
	}
}
