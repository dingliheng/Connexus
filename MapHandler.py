import json
import webapp2
from google.appengine.api import users
import Connexus
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

      raw_data = { "count": 10785236,
      "photos": [{"photo_id": 27932, "photo_title": "Atardecer en Embalse", "photo_url": "http://www.panoramio.com/photo/27932", "photo_file_url": "http://mw2.google.com/mw-panoramio/photos/medium/27932.jpg", "longitude": -64.404945, "latitude": -32.202924, "width": 500, "height": 375, "upload_date": "25 June 2006", "owner_id": 4483, "owner_name": "Miguel Coranti", "owner_url": "http://www.panoramio.com/user/4483"}
      ,
      {"photo_id": 522084, "photo_title": "In Memoriam Antoine de Saint Exupry", "photo_url": "http://www.panoramio.com/photo/522084", "photo_file_url": "http://mw2.google.com/mw-panoramio/photos/medium/522084.jpg", "longitude": 17.470493, "latitude": 47.867077, "width": 500, "height": 350, "upload_date": "21 January 2007", "owner_id": 109117, "owner_name": "Busa Pter", "owner_url": "http://www.panoramio.com/user/109117"}
      ,
      {"photo_id": 1578881, "photo_title": "Rosina Lamberti,Sunset,Templestowe , Victoria, Australia", "photo_url": "http://www.panoramio.com/photo/1578881", "photo_file_url": "http://mw2.google.com/mw-panoramio/photos/medium/1578881.jpg", "longitude": 145.141754, "latitude": -37.766372, "width": 500, "height": 474, "upload_date": "01 April 2007", "owner_id": 140796, "owner_name": "rosina lamberti", "owner_url": "http://www.panoramio.com/user/140796"}
      ,
      {"photo_id": 7593894, "photo_title": "fff", "photo_url": "http://www.panoramio.com/photo/7593894", "photo_file_url": "http://mw2.google.com/mw-panoramio/photos/medium/7593894.jpg", "longitude": 110.424957, "latitude": 24.781747, "width": 500, "height": 375, "upload_date": "04 February 2008", "owner_id": 161470, "owner_name": "John Su", "owner_url": "http://www.panoramio.com/user/161470"}
      ]}
      data = json.dumps(raw_data)
      self.response.write(data)
