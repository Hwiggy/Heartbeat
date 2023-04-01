import express from 'express';
import timestamp from 'unix-timestamp';
import * as WebSocket from 'ws'
import 'map.stringify';

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
    let data = Array.from(graph).map(elem => ({
        "x": timestamp.toDate(elem[0]).toDateString(),
        "y": elem[1]
    }))
    res.render('index', {data: JSON.stringify(data)})
})
wss.on('connection', ws => {
    ws.on('error', console.error)
    ws.on('message', data => {
        graph.set(`${timestamp.now() * 1000}`, `${data}`)
        console.log(`received: ${data}`)
        ws.send(`replying for ${data}`)
    })
})
