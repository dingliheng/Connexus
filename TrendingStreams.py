from datetime import datetime
import datetime
import logging

__author__ = 'yusun'
import cgi
import urllib
import webapp2
import CreateStream
import Connexus
from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users
from CreateStream import Stream
from google.appengine.api import mail

class TrendStreams(webapp2.RequestHandler):
    num = -1
    initial_num = -1
    def get(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
        while len(Connexus.count_queue) > 0:
            datetime_in_queue = Connexus.count_queue.pop()
            if (datetime.datetime.now() - datetime_in_queue[1]) >= datetime.timedelta(hours=1):
                stream = Stream.query(Stream.name == datetime_in_queue[0]).fetch(1)[0]
                stream.num_of_late_views = stream.num_of_late_views - 1
            else:
                Connexus.count_queue.appendleft(datetime_in_queue)
                break
        streams = Stream.query().order(-Stream.num_of_late_views).fetch(3)
        logging.info("count now is " + str(TrendStreams.num) + " " + str(TrendStreams.initial_num))
        if(TrendStreams.num == 0):
            TrendStreams.num = TrendStreams.initial_num
            # Prepare for the mail content
            mail_content = "The most popular three streams are as follows:\n"
            for stream in streams:
                mail_content = mail_content + str(stream.name) + " has been recently viewed for " + str(stream.num_of_late_views) + " times!\n"
            mail_content = mail_content + "Thank you for following us~\n"
            # Send the notification to specified user
            email="nima.dini@@utexas.edu"
            message = mail.EmailMessage()
            message.sender = "info@connexuselvis.appspotmail.com"
            message.to = email
            message.subject = "Check the most popular three streams!"
            message.body = mail_content
            message.send()

        TrendStreams.num = TrendStreams.num - 1
        # In order to overstack
        if TrendStreams.num == -100:
            TrendStreams.num = -1

        template_values = {
            'streams': streams,
            # 'user_id': user.user_id(),
            'url': url,
            'url_linktext': url_linktext,
        }

        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/TrendingStreams.html')
        self.response.write(template.render(template_values))

    def post(self):

        TrendStreams.num = int(self.request.get("t"))
        TrendStreams.initial_num = int(self.request.get("t"))


        # self.response.write(self.request.get("t"))
        self.redirect('/trend')

app = webapp2.WSGIApplication([
                               ('/trend', TrendStreams),
                               ], debug=True)