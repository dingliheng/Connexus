__author__ = 'Liheng'
import webapp2
from CreateStream import CreateNewStream
import CreateStream
import json

class Android_ViewAllStreams(webapp2.RequestHandler):
    def get(self):
        streams = CreateStream.Stream.query().fetch(16)
        streams = sorted(streams, key=lambda k: k.date,reverse = True)
        streams_URLs = []
        streams_Names = []
        for stream in streams:
            streams_URLs.append(stream.cover)
            streams_Names.append(stream.name)
        dictPassed = {'cover_urls':streams_URLs,"names":streams_Names}
        jsonObj = json.dumps(dictPassed,sort_keys=True,indent=4, separators=(',', ': '))
        self.response.write(jsonObj)

app = webapp2.WSGIApplication([
                               ('/android_viewall', Android_ViewAllStreams),
                               ], debug=True)