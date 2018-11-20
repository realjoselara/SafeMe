// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp({
    databaseURL: 'https://ssw695-3ec44.firebaseio.com/'

});

// Function calls
exports.sendNotificationAlert = functions.database.ref('/Notifications/{pushId}')
    .onWrite(event  => {
        console.log(event.after);
        return event.after;
    });

