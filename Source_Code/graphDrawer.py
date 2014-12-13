#!/usr/bin/env python

  # Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at

  #     http://www.apache.org/licenses/LICENSE-2.0

  # Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import sys
import gv
import json
from json import JSONDecoder
from datetime import datetime
from pygraph.classes.graph import graph
from pygraph.classes.digraph import digraph
from pygraph.readwrite.dot import write
from collections import defaultdict

ROOT_SPAN_ID = 0x74ace

def buildGraph(nid):
  for child in spansByParent[nid]:
    desc = spansBySpanId[child]["Description"] + "\n" + "(stop=" + str(spansBySpanId[child]["Stop"]) +")" + "\n" + "(start=" + str(spansBySpanId[child]["Start"]) + ")" + "\n" + "(traceid=" + str(spansBySpanId[child]["TraceID"]) + ")" + "\n" + "(spanid=" + str(spansBySpanId[child]["SpanID"]) + ")" + "\n" + "(parentid=" + str(spansBySpanId[child]["ParentID"]) +")"
    #graphviz can't handle '\'
    desc = desc.replace("\\", "")
    gr.add_node(child, [("label", desc)])
    gr.add_edge((nid, child))
    buildGraph(child)

def loads_invalid_obj_list(s):
  decoder = JSONDecoder()
  objs = [decoder.decode(x) for x in s.replace(" ", "-").split()]
  return objs

nodes = loads_invalid_obj_list(sys.stdin.read().strip())
print("i am here now")
print(nodes)
spansBySpanId = {s["SpanID"]:s for s in nodes}
spansByParent = defaultdict(set)

for node in spansBySpanId.values():
  spansByParent[node["ParentID"]].add(node["SpanID"])

count = 0
for x in spansByParent[ROOT_SPAN_ID]:
  count += 1
  gr = digraph()
  desc = spansBySpanId[x]["Description"] + "\n" + "(stop=" + str(spansBySpanId[x]["Stop"]) +")" + "\n" + "(start=" + str(spansBySpanId[x]["Start"]) + ")" + "\n" + "(traceid=" + str(spansBySpanId[x]["TraceID"]) + ")" + "\n" + "(spanid=" + str(spansBySpanId[x]["SpanID"]) + ")" + "\n" + "(parentid=" + str(spansBySpanId[x]["ParentID"]) +")"
  desc = desc.replace("\\", "")
  gr.add_node(x, [("label",desc)])
  buildGraph(x)
  dot = write(gr)
  gvv = gv.readstring(dot)
  gv.layout(gvv,'dot')
  gv.render(gvv,'png','./graphs/' + str(datetime.now()) + str(spansBySpanId[x]["Description"])[:10] + '.png')

print("Created " + str(count)  + " images.")
