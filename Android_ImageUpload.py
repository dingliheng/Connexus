__author__ = 'Liheng'

import webapp2
from CreateStream import Stream, Picture
import datetime
from google.appengine.ext.webapp import blobstore_handlers
import random

class Android_ImageUpload(blobstore_handlers.BlobstoreUploadHandler):

    def post(self):

        stream_name = self.request.params.get('stream_name')
        stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
        tags = self.request.params.get('tags')
        stream.tags.append(tags)
        upload = self.get_uploads()[0]
        stream.pictures.append(Picture(blob_key = upload.key(),
                                       date = datetime.datetime.now(),
                                       longitude = random.uniform(-150,150),
                                       latitude = random.uniform(-85,85)))
        stream.put();
        stream.put();

app = webapp2.WSGIApplication([
                               ('/Android_ImageUpload', Android_ImageUpload),
                               ], debug=True)
