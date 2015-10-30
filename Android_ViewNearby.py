__author__ = 'Liheng'
import  webapp2
import CreateStream
from CreateStream import Stream
import json
from google.appengine.api.images import get_serving_url

class Android_Nearby(webapp2.RequestHandler):

    def get(self):
        user_latitude = self.request.get('latitude')
        user_longitude = self.request.get('longitude')
        streams = CreateStream.Stream.query().fetch(50)
        pictures = []
        picture_urls = []
        pictures_distance = []
        for stream in streams:
            for picture in stream.pictures:
                pictures.append(picture)
                pictures_distance.append((picture.latitude-float(user_latitude))**2+(picture.latitude-float(user_latitude))**2)
        pictures.sort(cmp=None,key=lambda  x:x.date,reverse=True)
        # pictures.sort(cmp=None,key=lambda  x:((x.latitude-float(user_latitude))**2+(x.latitude-float(user_latitude))**2),reverse=False)
        pictures_distance.sort()
        if len(pictures)>16:
            pictures = pictures[0:15]
        for picture in pictures:
            picture_urls.append(get_serving_url(picture.blob_key, size=None, crop=False))
        dictPassed = {'picture_urls':picture_urls,"distance":pictures_distance}
        jsonObj = json.dumps(dictPassed,sort_keys=True,indent=4, separators=(',', ': '))
        self.response.write(jsonObj)

app = webapp2.WSGIApplication([
                               ('/android_viewnearby', Android_Nearby),
                               ], debug=True)