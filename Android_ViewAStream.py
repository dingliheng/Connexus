__author__ = 'Liheng'
import  webapp2
from CreateStream import Stream
import json
from google.appengine.api.images import get_serving_url

class Android_ViewAStream(webapp2.RequestHandler):

    def get(self):
        stream_name = self.request.get('stream_name')
        stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
        picture_urls = []
        for picture in stream.pictures:
            picture_urls.append(get_serving_url(picture.blob_key, size=None, crop=False))
        dictPassed = {'picture_urls':picture_urls}
        jsonObj = json.dumps(dictPassed,sort_keys=True,indent=4, separators=(',', ': '))
        self.response.write(jsonObj)

app = webapp2.WSGIApplication([
                               ('/android_viewsingle', Android_ViewAStream),
                               ], debug=True)