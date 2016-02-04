#! /usr/bin/python

# Import modules for CGI handling 
import cgi, cgitb, sys, json, urllib, MySQLdb, uuid, time, os, urlparse

#cgitb.enable()

#Open database connection
connection = MySQLdb.connect (host = "localhost", user = "cp341mobile", passwd = "blueFoxMoon", db = "ereca")

#initialize cursor
cursor = connection.cursor()

pic_dir = "./ereca_pics"

#boolean flag for a bad http request
flag = False;

#Still pretty messy

#get action from url
qs = urlparse.parse_qs(os.environ['QUERY_STRING'])
action = qs.get('action')[0]


#addNote flow
if action == "addNote":
	print "Content-type:text/html\r\n\r\n"
	#Create instance of FieldStorage 
	try:
		form = cgi.FieldStorage()
		data = form.value
		decoded_data = urllib.unquote(data).decode('utf8')
		json_data = json.loads(decoded_data)
		flag = True;
	except:
		print "POST data formatted incorrectly."


	if flag == True:
		#get Note info from JSON
		username = json_data['user']
		noteText = str(json_data['noteText']).decode('utf8')
		geo_lat = str(json_data['lat'])
		image_path = "place.holder"
		geo_lon = str(json_data['lon'])
		timestamp = time.strftime('%Y-%m-%d %H:%M:%S')
		#time_stamp = time.strptime(str(json_data['date']), '%Y-%m-%d %H:%M:%S')
		#date_stamp = time_stamp.strftime('%Y-%m-%d %H:%M:%S')
		if json_data['image']:
			image_path = pic_dir+"/"+username+"/"+str(json_data['date'])+".png"
			image_data = json_data['image']
			if not os.path.exists(pic_dir+"/"+username):
				os.makedirs(pic_dir+"/"+username)
				
			image_file = open(image_path,"wb")
			image_file.write(image_data.decode('base64'))
			image_file.close()

		#Get userId from userTable
		cursor.execute("SELECT userId FROM userTable WHERE username = %s", username)
		user_id = str(cursor.fetchone()[0])
		#Generate note UUID
		noteId = str(uuid.uuid1())

		#Put note into database
		cursor.execute("INSERT INTO noteTable(noteId, userId, text, filepath, lat, lon, timestamp) VALUES(%s, %s, %s, %s, %s, %s, %s)", [noteId, user_id, noteText, image_path, geo_lat, geo_lon, timestamp])
		#Commit the changes
		connection.commit()
		print action + " successful"
	else:
		print action + " failed"

#getNote workflow
elif action == "getNote":
	username = 


else:
	print "Action not recognised... Sorry bro."


