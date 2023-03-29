import express from 'express';
import * as WebSocket from 'ws'

const config = require('config.json')
const app = express()
const server = app.listen(config.port, config.host, () => {
    console.log("Heartbeat webservice is now online")
})
const wss = new WebSocket.WebSocketServer({ server })

wss.on('connection', ws => {
    ws.on('error', console.error)
    ws.send("Welcome to WS")
    ws.on('message', data => {
        console.log(`received: ${data}`)
        ws.send(`replying for ${data}`)
    })
})

import passport from 'passport';
import Strategy from 'passport-discord';

passport.use("discord", new Strategy(config.authentication.discord,
    (acc, ref, prof, cb) => {
        console.log(prof)
    }
))

app.get('/auth', passport.authenticate("discord"))
app.get('/auth/callback', passport.authenticate("discord", {
    failureRedirect: "/"
}), (req, res) => {

})
