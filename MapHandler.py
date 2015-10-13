import json
import logging
import random
import datetime
from google.appengine.api.images import get_serving_url
import webapp2
from google.appengine.api import users
import Connexus
from CreateStream import Stream

__author__ = 'yusun'
class MapHandler(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'

        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'


        template_values = {

            'user_id': user.user_id(),
            'url': url,
            'url_linktext': url_linktext,
        }

        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/Map.html')
        self.response.write(template.render(template_values))

    def post(self):

      stream_name = self.request.get('stream_name')

      stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
      user = users.get_current_user()

      raw_data = { "count": 0,
      "photos": [

      ]}
      i = 1
      start = int(self.request.get("start"))
      end = int(self.request.get("end"))


      for picture in stream.pictures:
        if (picture.date - datetime.datetime.now()) >= datetime.timedelta(days=start) and (picture.date - datetime.datetime.now()) <= datetime.timedelta(days=end):
            raw_data['photos'].append({"photo_id": i, "photo_title": "Picture " + str(i), "photo_url": get_serving_url(picture.blob_key), "photo_file_url": get_serving_url(picture.blob_key), "longitude": random.randint(-150, 150), "latitude": random.randint(-85, 85), "width": 500, "height": 375, "upload_date": "04 February 2015", "owner_id": 161470, "owner_name": str(user.email()), "owner_url": " "})
            raw_data['count'] = raw_data['count'] + 1
            i = i + 1


      data = json.dumps(raw_data)
      self.response.write(data)
