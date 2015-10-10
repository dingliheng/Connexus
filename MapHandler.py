import json
import logging
import random
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


      raw_data = { "count": 0,
      "photos": [
      ]}
      i = 1

      for image_url in stream.blob_key:
        raw_data['photos'].append({"photo_id": i, "photo_title": "live", "photo_url": "http://www.panoramio.com/photo/7593894", "photo_file_url": get_serving_url(image_url), "longitude": random.randint(-100, 100), "latitude": random.randint(-100, 100), "width": 500, "height": 375, "upload_date": "04 February 2008", "owner_id": 161470, "owner_name": "John Su", "owner_url": "http://www.panoramio.com/user/161470"})
        raw_data['count'] = raw_data['count'] + 1
        i = i + 1

      data = json.dumps(raw_data)
      self.response.write(data)
