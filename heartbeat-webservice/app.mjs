import express from 'express';
import * as WebSocket from 'ws'

const app = express()
const server = app.listen(8080, "0.0.0.0", () => {
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
