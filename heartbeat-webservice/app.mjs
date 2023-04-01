import express from 'express';
import timestamp from 'unix-timestamp';
import * as WebSocket from 'ws'
import 'map.stringify';
import moment from 'moment';
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
    let data = Array.from(graph).map(elem => {
        let date = timestamp.toDate(Number(elem[0]) / 1000)
        let dateStr = moment(date).format("MM-DD-yyyy hh:mm:ss")
        return {
            "x": dateStr,
            "y": elem[1]
        }
    })
    res.render('index', {data: JSON.stringify(data)}, () => {
        setInterval(() => res.reload(), 1000)
    })
})
wss.on('connection', ws => {
    ws.on('error', console.error)
    ws.on('message', data => {
        graph.set(`${timestamp.now() * 1000}`, `${data}`)
        console.log(`received: ${data}`)
        ws.send(`replying for ${data}`)
    })
})
