import logging
import os
import random
import jinja2
from Connexus import User
import Connexus
from CreateStream import Stream, Picture
import datetime

__author__ = 'yusun'
import cgi
import urllib
import webapp2


from google.appengine.api import users
from google.appengine.ext import blobstore
from google.appengine.ext import ndb
from google.appengine.ext.webapp import blobstore_handlers

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


class ViewStream(webapp2.RequestHandler):

    def get(self):
        stream_name = self.request.get('stream_name')
        self.response.write(stream_name + " ")
        stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
        # user = User.query(User.email == users.get_current_user().email)
        # Check if the user logs in
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
        # if stream.num_of_views:
        stream.num_of_views = stream.num_of_views + 1
        stream.num_of_late_views = stream.num_of_late_views + 1
        Connexus.count_queue.append((stream.name, datetime.datetime.now()))
        stream.put()
        self.response.write("stream has been viewed: " + str(stream.num_of_views))



        upload_url = blobstore.create_upload_url('/upload_photo?stream_name='+str(stream_name))
        # self.redirect('/img?stream_name='+str(stream_name))

        cover_url = stream.cover

        tags = stream.tags[0]
        template_values = {
            'stream_name': stream_name,
            # "stream.num_of_views":stream.num_of_views,
            "stream_tags": tags,
            "stream_pictures":stream.pictures,
            'cover_url': cover_url,
            'user_id': user.email(),
            'url': url,
            'url_linktext': url_linktext,
            'upload_url': upload_url,
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/ViewASingleStream.html')
        self.response.write(template.render(template_values))

    def post(self):
        stream_name = self.request.get('stream_name')
        upload_url = blobstore.create_upload_url('/upload_photo?stream_name='+str(stream_name))
        self.response.write(upload_url)



# A custom datastore model for associating users with uploaded files.
class UserPhoto(ndb.Model):
  user = ndb.StringProperty()
  # blob_key = blobstore.BlobReferenceProperty()
  blob_key = ndb.BlobKeyProperty()


# [START upload_handler]
class PhotoUploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        try:

            stream_name = self.request.get('stream_name')
            stream = Stream.query(Stream.name == stream_name).fetch(1)[0]

            if self.get_uploads():
                logging.error("something")
            else:
                logging.error("none")
            for upload in self.get_uploads():
                stream.num_of_pics = stream.num_of_pics+1
                stream.date = datetime.datetime.now()
                stream.pictures.append(Picture(blob_key = upload.key(),
                                               date = datetime.datetime.now(),
                                               longitude = random.uniform(-150,150),
                                               latitude = random.uniform(-85,85)))
                stream.put()
                stream.put()


            # self.redirect('/view?stream_name=%s' % stream_name)
            # self.response.write("rrrr")
        except:
            self.error(500)
# [END upload_handler]


# [START download_handler]
class ViewPhotoHandler(blobstore_handlers.BlobstoreDownloadHandler):
    def get(self, photo_key):

        if not blobstore.get(photo_key):
            self.error(404)
        else:
            self.send_blob(photo_key)
# [END download_handler]




# [START subsribe this stream]

class Subscribe(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()
        if user:

            getUser = User.query(User.email == user.email())
            # self.response.write(str(getUser.fetch(1)))
            global currentUser
            if getUser.fetch(1):
                currentUser = getUser.fetch(1)[0]

            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()

            stream_name = self.request.get('stream_name')
            stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
            currentUser.streams_subscribed.append(stream.key)
            currentUser.put()
            currentUser.put()
            self.redirect('/')

        # User has not been logged in
        else:
             self.redirect(users.create_login_url(self.request.uri))


