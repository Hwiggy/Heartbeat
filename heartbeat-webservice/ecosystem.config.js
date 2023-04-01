module.exports = {
    apps : [{
        name: "heartbeat",
        script: "./app.mjs",
        env: {
            NODE_ENV: "development",
        },
        env_production: {
            NODE_ENV: "production",
        }
    }]
}