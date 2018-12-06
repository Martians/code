from invoke import Collection

import release, docs

ns = Collection()
ns.add_collection(release)
ns.add_collection(docs)