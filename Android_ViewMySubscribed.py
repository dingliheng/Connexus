from Connexus import User

__author__ = 'yusun'
import webapp2
from CreateStream import CreateNewStream
import CreateStream
import json
class Android_ViewMySubscibed(webapp2.RequestHandler):
    def get(self):
        user_email = self.request.get("user")
        subscribed_streams = []
        getUser = User.query(User.email == user_email)
        if getUser.fetch(1):
                currentUser = getUser.fetch(1)[0]
                for stream_key in currentUser.streams_subscribed:
                    stream_subscribed = stream_key.get()
                    if stream_subscribed:
                        subscribed_streams.append(stream_subscribed)
                        streams_URLs = []
                        streams_Names = []
                        for stream in subscribed_streams:
                            streams_URLs.append(stream.cover)
                            streams_Names.append(stream.name)
                            dictPassed = {'cover_urls':streams_URLs,"names":streams_Names}
                            jsonObj = json.dumps(dictPassed,sort_keys=True,indent=4, separators=(',', ': '))
                            self.response.write(jsonObj)

app = webapp2.WSGIApplication([
                               ('/android_viewmysubscribed', Android_ViewMySubscibed),
                               ], debug=True)