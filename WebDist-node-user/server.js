const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcrypt");
const dotenv = require("dotenv");
const Eureka = require('eureka-js-client').Eureka;

dotenv.config();

const User = require("./models/User");

const app = express();
const PORT = 3000;

app.use(express.json());
app.use(cors());

mongoose
  .connect(process.env.MONGODB_URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log("Connected to MongoDB");
  })
  .catch((err) => {
    console.error("MongoDB connection error:", err);
  });


  const client = new Eureka({
    instance: {
      app: 'node-login-service',
      hostName: 'user', // should match service name or container name in docker-compose
      ipAddr: 'user', // use container name to avoid needing IP resolution
      port: {
        '$': 3000,
        '@enabled': true,
      },
      vipAddress: 'node-login-service',
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: 'eureka-server', // Eureka's container hostname
      port: 8761,
      servicePath: '/eureka/apps/',
      maxRetries: 10, 
      requestRetryDelay: 2000,
    },
  });
        
    
  

client.start(err => {
  console.log('Eureka client started:', err || 'Service registered with Eureka');
});

app.listen(PORT, '0.0.0.0', () => {
  console.log("Listening on port " + PORT);
});

app.post("/api/signup", async (req, res) => {
  try {
    const { email, password, username, role } = req.body;

    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(500).json({ message: "Email already in use" });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = new User({
      email,
      password: hashedPassword,
      username,
      role,
    });

    await newUser.save();
    res.status(201).json({ message: "User registered successfully" });
  } catch (error) {
    console.error("Error during signup:", error);
    res.status(500).json({ message: "Server error during signup" });
  }
});

app.post("/api/signin", async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(400).json({ message: "Invalid email or password" });
    }
    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(400).json({ message: "Invalid email or password" });
    }
    const token = jwt.sign(
      {
        userId: user._id,
        email: user.email,
        role: user.role,
      },
      process.env.JWT_SECRET,
      {
        expiresIn: "1h",
      }
    );

    res.json({ token, user: { username: user.username }, role: { role: user.role } });
  } catch (error) {
    console.error("Error during signin:", error);
    res.status(500).json({ message: "Server error during signin" });
  }
});

function authenticateToken(req, res, next) {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];

  if (!token) {
    return res.status(401).json({ message: "No token provided, authorization denied" });
  }

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(403).json({ message: "Invalid or expired token" });
  }
}

app.get("/api/profile", authenticateToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const user = await User.findById(userId).select("-password");

    if (!user) {
      return res.status(404).json({ message: "User not found." });
    }
    return res.json({ user });
  } catch (error) {
    console.log("Error fetching profile", error);
    res.status(500).json({ message: "server error" });
  }
});
