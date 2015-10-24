__author__ = 'Liheng'

import webapp2
from CreateStream import CreateNewStream
import CreateStream
import json
import re

class Android_Search(webapp2.RequestHandler):
    def get(self):
        keyword = self.request.get("keywords")
        streams = CreateStream.Stream.query().fetch(50)
        pattern = re.compile('\S*'+keyword+'\S*',re.I)
        num_show = 3
        search_results = []
        streams_URLs = []
        streams_Names = []
        for stream in streams:
                    content = stream.name
                    for tag in stream.tags:
                        content = content+' '+tag
                    match = pattern.search(content)
                    if match:
                        search_results.append(stream)
        num_results = len(search_results)
        n = num_results/num_show
        r = num_results%num_show
        if r!=0:
            n = n + 1
        times = int(self.request.get("times"))
        if times < n :
            search_results = search_results[(times-1)*num_show:times*num_show]
            times = times+1
        else:
            if times==n:
                if search_results:
                    search_results = search_results[(times-1)*num_show:]
                    times = 1
            else:
                times = 1
        for stream in search_results:
            streams_URLs.append(stream.cover)
            streams_Names.append(stream.name)
        dictPassed = {'cover_urls':streams_URLs,"names":streams_Names,"num_results":num_results,"times":times}
        jsonObj = json.dumps(dictPassed,sort_keys=True,indent=4, separators=(',', ': '))
        self.response.write(jsonObj)

app = webapp2.WSGIApplication([
                               ('/android_search', Android_Search),
                               ], debug=True)