import express from "express";
import Fruit from "../models/Fruit.js";
import NutritionProfile from "../models/NutritionProfile.js";

const r = express.Router();

r.get("/resource/fruits/", async (req, res) => {
  const q = req.query.q || "";
  const items = await Fruit.find(q ? { name: { $regex: q, $options: "i" } } : {})
    .select("-__v").lean();
  res.json(items);
});

r.get("/resource/fruits/:id", async (req, res) => {
  const f = await Fruit.findById(req.params.id).populate("nutritionProfile").lean();
  if (!f) return res.sendStatus(404);
  res.json(f);
});


r.post("/resource/fruits", async (req, res) => {
  const payload = req.body || {};
  let np = null;
  if (payload.nutrition) {
    np = await NutritionProfile.create(payload.nutrition);
    payload.nutritionProfile = np._id;
  }
  const saved = await Fruit.create(payload);
  res.status(201).json(saved);
});

r.put("/resource/fruits/:id", async (req, res) => {
  const saved = await Fruit.findByIdAndUpdate(req.params.id, req.body, { new: true });
  if (!saved) return res.sendStatus(404);
  res.json(saved);
});

r.delete("/resource/fruits/:id", async (req, res) => {
  const f = await Fruit.findByIdAndDelete(req.params.id);
  if (!f) return res.sendStatus(404);
  res.sendStatus(204);
});

r.get("/_resource/test", async (req, res) => {
  const v = req.query.view || "nutritionProfile";
  const f = await Fruit.find().populate(v).lean();
  if (!f) return res.sendStatus(404);
  res.json(f);
});

export default r;

