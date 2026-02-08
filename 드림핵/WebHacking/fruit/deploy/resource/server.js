import cors from "cors";
import morgan from "morgan";
import express from "express";
import mongoose from "mongoose";

import Fruit from "./models/Fruit.js";
import fruitsRouter from "./routes/fruits.js";
import NutritionProfile from "./models/NutritionProfile.js";

const app = express();
app.use(cors({ origin: true, credentials: true }));
app.use(morgan("dev"));
app.use(express.json({ limit: "5mb" }));

const MONGO_URI = process.env.MONGO_URI || "mongodb://mongo:27017/fruitdb";

app.use("/api", fruitsRouter);

async function initDB() {

  await Promise.all([
    Fruit.deleteMany({}),
    NutritionProfile.deleteMany({})
  ]);


  const items = [
    { name: "Apple", price: 15000, imagePath: "/uploads/apple.png", season: "autumn", origin: "KR",
      description: "Crisp and juicy fruit, rich in fiber and vitamin C.", nutrition: { caloriesPer100g: 52, vitamins: [{name:"C", mg:4.6}] } },
    { name: "Strawberry", price: 19000, imagePath: "/uploads/strawberry.png", season: "spring", origin: "KR",
      description: "Sweet spring berry.", nutrition: { caloriesPer100g: 33, vitamins: [{name:"C", mg:59.0}] } },
    { name: "Durian",  price: 5000, imagePath: "/uploads/durian.png", season: "summer", origin: "TH",
      description: "King of fruits, strong aroma.", nutrition: { caloriesPer100g: 147, vitamins: [{name:"C", mg:19.7}] } },
    { name: "Banana", price: 3300, imagePath: "/uploads/banana.png", season: "all",    origin: "PH",
      description: "Soft and sweet.", nutrition: { caloriesPer100g: 89, vitamins: [{name:"B6", mg:0.4}] } },
    { name: "Melon",  price: 8700, imagePath: "/uploads/melon.png", season: "summer", origin: "KR",
      description: "Juicy summer melon.", nutrition: { caloriesPer100g: 34, vitamins: [{name:"C", mg:36.7}] } },
    { name: "Turnip",  price: 1500, imagePath: "/uploads/turnip.png", season: "fall",   origin: "KR",
      description: "Root vegetable, crisp.", nutrition: { caloriesPer100g: 28, vitamins: [{name:"C", mg:21.0}] } },
    { name: "Carrot", price: 1000, imagePath: "/uploads/carrot.png", season: "all",    origin: "KR",
      description: "Crunchy and sweet.", nutrition: { caloriesPer100g: 41, vitamins: [{name:"A", mg:835.0}] } },
    { name: "Chili Pepper", price: 5000, imagePath: "/uploads/pepper.png", season: "summer", origin: "KR",
        description: "Spicy and pepper used in Korean cuisine", nutrition: { caloriesPer100g: 40, vitamins: [{name:"A", mg:59.0}] } }
  ];

  for (const it of items) {
    const np = await NutritionProfile.create({
      fruitNameKey: it.name.toLowerCase(),
      caloriesPer100g: it.nutrition.caloriesPer100g,
      vitamins: it.nutrition.vitamins
    });
    await Fruit.create({
      name: it.name,
      description: it.description,
      price: it.price,
      imagePath: it.imagePath,
      season: it.season,
      origin: it.origin,
      nutritionProfile: np._id
    });
  }
  console.log(`[seed] fruit + nutrition inserted: ${items.length}`);
}

const port = process.env.PORT || 3000;
mongoose.connect(MONGO_URI).then(async () => {
  try { await initDB(); } catch (e) { console.error(`[seed] failed: ${e}`); }
  app.listen(port, () => console.log(`resource server up on ${port}`));
});
