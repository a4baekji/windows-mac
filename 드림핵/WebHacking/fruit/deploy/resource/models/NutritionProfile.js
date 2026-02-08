import mongoose from "mongoose";

const NutritionProfileSchema = new mongoose.Schema({
  fruitNameKey: { type: String, index: true },
  caloriesPer100g: Number,
  vitamins: [{ name: String, mg: Number }]
}, { timestamps: true });

export default mongoose.model("NutritionProfile", NutritionProfileSchema);