import express from 'express';
import timestamp from 'unix-timestamp';
import * as WebSocket from 'ws'
require('map.stringify')

import config from './config.json' assert { type: "json" }
const app = express()

app.set('view engine', 'pug')
app.use(express.static('public'))

const server = app.listen(config.port, config.host, () => {
    console.log("Heartbeat webservice is now online")
})
const wss = new WebSocket.WebSocketServer({ server })

const graph = new Map()
app.get('/', (req, res) => {
    res.render('index', { graph: Map.stringify(graph) })
})
wss.on('connection', ws => {
    ws.on('error', console.error)
    ws.on('message', data => {
        graph.set(`${timestamp.now()}`, data)
        console.log(`received: ${data}`)
        ws.send(`replying for ${data}`)
    })
})
