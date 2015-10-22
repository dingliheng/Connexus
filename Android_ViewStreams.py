__author__ = 'Liheng'
import webapp2
from CreateStream import CreateNewStream
import CreateStream
import json

class Android_ViewAllStreams(webapp2.RequestHandler):
    def get(self):
        streams = CreateStream.Stream.query().fetch(50)
        streams_URLs = []
        for stream in streams:
            streams_URLs.append(stream.cover)
        dictPassed = {'cover_urls':streams_URLs}
        jsonObj = json.dumps(dictPassed,sort_keys=True,indent=4, separators=(',', ': '))
        self.response.write(jsonObj)

app = webapp2.WSGIApplication([
                               ('/android_viewall', Android_ViewAllStreams),
                               ], debug=True)