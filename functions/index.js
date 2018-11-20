const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });


exports.sendNotification = function.database.ref('/Notifications').onCreate((snapshot, context) => {
    const message  = snapshot.val();
    console.log(message);

});


const admin = require('firebase-admin');
admin.initializeApp();