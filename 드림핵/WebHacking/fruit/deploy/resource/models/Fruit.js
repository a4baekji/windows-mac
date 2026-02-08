import mongoose from "mongoose";

const FruitSchema = new mongoose.Schema({
  name: { type: String, required: true, index: true },
  description: String,
  price: Number,
  season: String,
  origin: String,
  imagePath: String,
  nutritionProfile: { type: mongoose.Schema.Types.ObjectId, ref: "NutritionProfile" }
}, { timestamps: true });

export default mongoose.model("Fruit", FruitSchema);