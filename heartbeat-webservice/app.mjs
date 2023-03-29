import express from 'express';
import * as WebSocket from 'ws'

import config from './config.json' assert { type: "json" }
const app = express()


import passport from 'passport';
import Strategy from 'passport-discord';

passport.use("discord", new Strategy(config.authentication.discord,
    (acc, ref, prof, cb) => {
        console.log(prof)
        return cb(profile)
    }
))

app.get('/auth', passport.authenticate("discord"))
app.get('/auth/callback', passport.authenticate("discord", {
    failureRedirect: "/"
}), (req, res) => {
    res.redirect("/auth/success")
})

app.get('/auth/success', (req, res) => {
    res.send(200)
})

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
