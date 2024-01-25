node_factory = py_tf

node_factory.registerNamespace("dc", "http://purl.org/dc/elements/1.1/")
node_factory.registerNamespace("dcterms", "http://purl.org/dc/terms/")
node_factory.registerNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
node_factory.registerNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#")
node_factory.registerNamespace("schema", "http://schema.org/")
node_factory.registerNamespace("sh", "http://www.w3.org/ns/shacl#")
node_factory.registerNamespace("skos", "http://www.w3.org/2004/02/skos/core#")
node_factory.registerNamespace("owl", "http://www.w3.org/2002/07/owl#")
node_factory.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema#")


def create_solution(base):
    result = {}
    for attr in base:
        if base[attr] is not None:
            result[attr] = base[attr]
    return result


def T(str):
    return node_factory.term(str)


def var_to_attr(var_name):
    if not var_name.startswith("?"):
        raise ValueError("Variable name must start with '?'")
    if len(var_name) == 1:
        raise ValueError("Variable name too short")
    return var_name[1:]


def add_path_values(graph, subject, path, _set):
    if hasattr(path, 'getUri()'):
        matches = rdf_query(graph).match(subject, path, "?object").get_node_array("?object")
        _set.add_all(matches)
    elif isinstance(path, list):
        s = NodeSet()
        s.add(subject)
        for i in range(len(path)):
            a = s.to_array()
            s = NodeSet()
            for j in range(len(a)):
                add_path_values(graph, a[j], path[i], s)
        _set.add_all(s.to_array())
    elif hasattr(path, 'or'):
        for i in range(len(path['or'])):
            add_path_values(graph, subject, path['or'][i], _set)
    elif hasattr(path, 'inverse'):
        if path['inverse'].isURI():
            matches = rdf_query(graph).match("?subject", path['inverse'], subject).get_node_array("?subject")
            _set.add_all(matches)
        else:
            raise ValueError("Unsupported: Inverse paths only work for named nodes")
    elif hasattr(path, 'zeroOrOne'):
        add_path_values(graph, subject, path['zeroOrOne'], _set)
        _set.add(subject)
    elif hasattr(path, 'zeroOrMore'):
        walk_path(graph, subject, path['zeroOrMore'], _set, NodeSet())
        _set.add(subject)
    elif hasattr(path, 'oneOrMore'):
        walk_path(graph, subject, path['oneOrMore'], _set, NodeSet())
    else:
        raise ValueError("Unsupported path object: " + str(path))


def walk_path(graph, subject, path, _set, visited):
    visited.add(subject)
    s = NodeSet()
    add_path_values(graph, subject, path, s)
    a = s.to_array()
    _set.add_all(a)
    for i in range(len(a)):
        if a[i] not in visited:
            walk_path(graph, a[i], path, _set, visited)


class AbstractQuery:
    def __init__(self):
        pass

    def bind(self, var_name, bind_function):
        return BindQuery(self, var_name, bind_function)

    def filter(self, filter_function):
        return FilterQuery(self, filter_function)

    def limit(self, limit):
        return LimitQuery(self, limit)

    def match(self, s, p, o):
        return MatchQuery(self, s, p, o)

    def order_by(self, var_name):
        return OrderByQuery(self, var_name)

    def path(self, s, path, o):
        if path and hasattr(path, "getValue") and hasattr(path, "getUri"):
            return MatchQuery(self, s, path, o)
        else:
            return PathQuery(self, s, path, o)

    def add_all_nodes(self, var_name, _set):
        attr_name = var_to_attr(var_name)
        for sol in self.next_solution():
            node = sol[attr_name]
            if node:
                _set.add(node)

    def construct(self, subject, predicate, obj):
        results = []
        for sol in self.next_solution():
            s = None
            if isinstance(subject, str):
                if subject.startswith('?'):
                    s = sol[var_to_attr(subject)]
                else:
                    s = T(subject)
            else:
                s = subject

            p = None
            if isinstance(predicate, str):
                if predicate.startswith('?'):
                    p = sol[var_to_attr(predicate)]
                else:
                    p = T(predicate)
            else:
                p = predicate

            o = None
            if isinstance(obj, str):
                if obj.startswith('?'):
                    o = sol[var_to_attr(obj)]
                else:
                    o = T(obj)
            else:
                o = obj

            if s and p and o:
                results.append({'subject': s, 'predicate': p, 'object': o})
        return results

    def for_each(self, callback):
        for n in self.next_solution():
            callback(n)

    def for_each_node(self, var_name, callback):
        attr_name = var_to_attr(var_name)
        for sol in self.next_solution():
            node = sol[attr_name]
            if node:
                callback(node)

    def get_array(self):
        results = []
        for n in self.next_solution():
            results.append(n)
        return results

    def get_count(self):
        return len(self.get_array())

    def get_node(self, var_name):
        s = self.next_solution()
        if s is not None:
            self.close()
            return s[var_to_attr(var_name)]
        else:
            return None

    def get_node_array(self, var_name):
        results = []
        attr = var_to_attr(var_name)
        for k, v in (self.next_solution()).items():
            if k == attr:
                results.append(v)
        return results

    def get_node_set(self, var_name):
        results = NodeSet()
        attr = var_to_attr(var_name)
        for k, v in (self.next_solution()).items():
            if k == attr:
                results.append(v)
        return results

    def get_object(self, subject, predicate):
        sol = self.next_solution()
        if sol:
            self.close()
            s = sol[var_to_attr(subject)] if isinstance(subject, str) and subject.startswith('?') else T(subject)
            p = sol[var_to_attr(predicate)] if isinstance(predicate, str) and predicate.startswith('?') else T(
                predicate)
            if not s:
                raise Exception("get_object() called with None subject")
            if not p:
                raise Exception("get_object() called with None predicate")
            it = self.source.find(s, p, None)
            triple = it.next()
            it.close()
            return triple.object if triple else None
        return None

    def has_solution(self):
        if self.next_solution():
            self.close()
            return True
        else:
            return False


class PathQuery(AbstractQuery):
    def __init__(self, input_query, subject, path, _object):
        self.source = input_query.source
        self.input = input_query

        if isinstance(subject, str) and subject.startswith('?'):
            self.subject_attr = var_to_attr(subject)
        else:
            self.subject = subject

        if path is None:
            raise ValueError("Path cannot be unbound")

        if isinstance(path, str):
            self.path_ = T(path)
        else:
            self.path_ = path

        if isinstance(_object, str) and _object.startswith('?'):
            self.object_attr = var_to_attr(_object)
        else:
            self.object = _object

    def close(self):
        self.input.close()

    def next_solution(self):
        r = getattr(self, 'path_results', None)
        if r:
            n = r[self.path_index]
            result = create_solution(self.input_solution)
            if hasattr(self, 'object_attr'):
                result[self.object_attr] = n
            self.path_index += 1
            if self.path_index == len(r):
                delattr(self, 'path_results')  # Mark as exhausted
            return result

        # Pull from input
        self.input_solution = self.input.next_solution()
        if self.input_solution is not None:
            if hasattr(self, "subject_attr"):
                sm = self.input_solution[self.subject_attr]
            else:
                sm = self.subject
            if sm is None:
                raise ValueError("Path cannot have an unbound subject")

            if hasattr(self, "object_attr"):
                if self.object_attr in self.input_solution:
                    om = self.input_solution[self.object_attr]
                else:
                    om = None
            else:
                om = self.object
            path_results_set = NodeSet()
            add_path_values(self.source, sm, self.path_, path_results_set)
            self.path_results = path_results_set.to_array()

            if len(self.path_results) == 0:
                delattr(self, 'path_results')
            elif om:
                delattr(self, 'path_results')
                if om in path_results_set:
                    return self.input_solution
            else:
                self.path_index = 0
            return self.next_solution()
        else:
            return None


class MatchQuery(AbstractQuery):
    def __init__(self, input_query, s, p, o):
        self.source = input_query.source
        self.input = input_query
        if isinstance(s, str):
            if s.startswith('?'):
                self.sv = var_to_attr(s)
            else:
                self.s = T(s)
        else:
            self.s = s

        if isinstance(p, str):
            if p.startswith('?'):
                self.pv = var_to_attr(p)
            else:
                self.p = T(p)
        else:
            self.p = p

        if isinstance(o, str):
            if o.startswith('?'):
                self.ov = var_to_attr(o)
            else:
                self.o = T(o)
        else:
            self.o = o

    def close(self):
        self.input.close()
        if hasattr(self, 'own_iterator'):
            self.own_iterator.close()

    def next_solution(self):
        oit = getattr(self, 'own_iterator', None)

        if oit:
            n = oit.next()
            if n is not None:
                result = create_solution(self.input_solution)
                if hasattr(self, 'sv'):
                    result[self.sv] = n.getSubject()
                if hasattr(self, 'pv'):
                    result[self.pv] = n.getPredicate()
                if hasattr(self, 'ov'):
                    result[self.ov] = n.getObject()
                return result
            else:
                delattr(self, 'own_iterator')  # Mark as exhausted

        # Pull from input
        self.input_solution = self.input.next_solution()
        if len(self.input_solution) >= 0:
            if hasattr(self, 'sv'):
                sm = self.input_solution[self.sv]
            else:
                sm = self.s

            if hasattr(self, 'pv'):
                pm = self.input_solution[self.pv]
            else:
                pm = self.p

            if hasattr(self, 'ov'):
                if self.ov in self.input_solution:
                    om = self.input_solution[self.ov]
                else:
                    om = None
            else:
                om = self.o
            self.own_iterator = self.source.find(sm, pm, om)
            return self.next_solution()
        else:
            return None


class StartQuery(AbstractQuery):
    def __init__(self, source, initial_solution=None):
        self.source = source
        if initial_solution and len(initial_solution) > 0:
            self.solution = initial_solution
        else:
            self.solution = [{}]

    def close(self):
        pass

    def next_solution(self):
        if self.solution:
            if len(self.solution) > 0:
                return self.solution.pop(0)
            else:
                del self.solution


class NodeSet:
    def __init__(self):
        self.values = []

    def add(self, node):
        if not self.contains(node):
            self.values.append(node)

    def add_all(self, nodes):
        for node in nodes:
            self.add(node)

    def contains(self, node):
        for value in self.values:
            if value.equals(node):
                return True
        return False

    def for_each(self, callback):
        for value in self.values:
            callback(value)

    def size(self):
        return len(self.values)

    def to_array(self):
        return self.values.copy()

    def __str__(self):
        size = self.size()
        arr = self.to_array()
        str_repr = f"NodeSet({size}): ["
        str_repr += ", ".join(map(str, arr))
        str_repr += "]"
        return str_repr


class BindQuery(AbstractQuery):
    def __init__(self, input_query, var_name, bind_function):
        self.attr = var_to_attr(var_name)
        self.source = input_query.source
        self.input = input_query
        self.bind_function = bind_function

    def close(self):
        self.input.close()

    def next_solution(self):
        result = self.input.next_solution()
        if result is None:
            return None
        else:
            new_node = self.bind_function(result)
            if new_node:
                result[self.attr] = new_node
            return result


def rdf_query(graph, initial_solution=None):
    return StartQuery(graph, initial_solution)
