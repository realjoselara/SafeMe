// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Function calls
exports.sendNotificationAlert = functions.database
    .ref('/Notifications/{pushId}')
    .onWrite((snapshot, context)  => {

        var location = context.params.Location;
        var type = context.params.Type;
        var description = context.params.Description;
        var date = context.params.Date;
        console.log(`New message ${type}, location ${location}, description: ${description}, date: ${date}`)

        const messageData = snapshot.val();
        console.log(messageData)
        return messageData;

    });
