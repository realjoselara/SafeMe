// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotificationAlert = functions.database
    .ref('/Notifications/{pushId}')
    .onWrite(event => {

        const description = event.after._data.Description
        const location = event.after._data.Location
        const type = event.after._data.Type

        const payload = {
            notification: {
                title: type,
                body:  `Description: ${description}, Location: ${location['0']} ${location['1']}`,
                sound: "default"
            },
        };

        //Create an options object that contains the time to live for the notification and the priority
        const options = {
            priority: "high",
            timeToLive: 60 * 60 * 24
        };

        return admin.messaging().sendToTopic("pushNotifications", payload, options);
    });

