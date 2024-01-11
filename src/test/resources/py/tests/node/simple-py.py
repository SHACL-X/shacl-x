def simple(value):
    if not value.isURI():
        return "IRIs expected"
