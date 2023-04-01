import config from 'config.json' assert { type: "json" }
import express from "express";
const router = new express.Router()

import passport from 'passport';
import Strategy from 'passport-discord';

passport.use("discord", new Strategy(config.authentication.discord,
    (acc, ref, prof, cb) => {
        console.log(prof)
        return cb(prof)
    }
))

router.get('/auth', passport.authenticate("discord"))
router.get('/auth/callback', passport.authenticate("discord", {
    failureRedirect: "/"
}), (req, res) => {
    res.redirect("/auth/success")
})

router.get('/auth/success', (req, res) => {
    res.send(200)
})

module.exports = router