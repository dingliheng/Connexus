import os
import jinja2
from Connexus import User
from CreateStream import Stream

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
        self.response.write(stream_name)
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


        # for picture in stream.blob_key:
        #     self.send_blob(picture)
            # self.response.headers['Content-Type'] = 'image/png'
            # self.response.out.write("j")
            # self.response.out.write('<img src="/img?stream_name=%s"></img>' %
            #                         stream_name)
        # self.response.out.write(stream.picture[0])
        for blob_key in stream.blob_key:
            self.response.out.write('<img src="/view_photo/%s"</img>'% blob_key)

        upload_url = blobstore.create_upload_url('/upload_photo?stream_name='+str(stream_name))
        # self.redirect('/img?stream_name='+str(stream_name))

        cover_url = stream.cover
        template_values = {
            'stream_name': stream_name,
            'cover_url': cover_url,
            'user_id': user.user_id(),
            'url': url,
            'url_linktext': url_linktext,
            'upload_url': upload_url,
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/ViewASingleStream.html')
        self.response.write(template.render(template_values))

        # upload_url = blobstore.create_upload_url('/upload_photo?stream_name='+str(stream_name))
        # [END upload_url]
        # [START upload_form]
        # The method must be "POST" and enctype must be set to "multipart/form-data".
        self.response.out.write('<html><body>')
        self.response.out.write('<form action="%s" method="POST" enctype="multipart/form-data">' % upload_url)
        self.response.out.write('''Upload File: <input type="file" name="file"><br> <input type="submit"
            name="submit" value="Submit"> </form></body></html>''')
        # [END upload_form]

    # def post(self):
    #     stream_name = self.request.get('stream_name')
    #     stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
    #     picture = self.request.get('new_img')
    #     stream.picture.append(picture)
    #     stream.num_of_pics = stream.num_of_pics + 1
    #     stream.put()
    #
    #     query_params = {'stream_name': stream_name}
    #     self.redirect('/view?' + urllib.urlencode(query_params))

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

            upload = self.get_uploads()[0]
            stream.blob_key.append(upload.key())
            stream.put()
            self.redirect('/view_photo/%s' % upload.key())

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

class Image(webapp2.RequestHandler):
    def get(self):
        stream_name = self.request.get('stream_name')
        stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
        self.response.write(stream.picture)
        if stream.picture[0]:
            self.response.write("aa")
            # self.response.headers['Content-Type'] = 'image/png'
            self.response.write(stream.picture[1])
        else:
            self.response.out.write('No image')
        # self.response.out.write("t")
        # for picture in stream.picture:
        #     self.response.headers['Content-Type'] = 'image/png'
        #     self.response.out.write("j")
        #     self.response.out.write(picture)
        # else:
        #     self.response.out.write('No image')


# [START subsribe this stream]

class Subsribe(webapp2.RequestHandler):
    def post(self):

        user = User.query(User.email == users.get_current_user().email)
        if user:
            stream = self.request.get('stream')
            user.fetch(1)[0].stream_subsribed.append(stream.key())
            self.response.out.write('Subsribe success!')
        else:
             self.redirect(users.create_login_url(self.request.uri))
