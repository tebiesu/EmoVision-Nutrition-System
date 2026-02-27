import { expect, test } from "@playwright/test";

test("should finish main user flow", async ({ page }) => {
  await page.goto("/login");
  await page.getByTestId("login-username").fill("demo");
  await page.getByTestId("login-password").fill("demo");
  await page.getByTestId("login-submit").click();

  await expect(page).toHaveURL(/\/dashboard$/);
  await expect(page.getByTestId("dashboard-title")).toBeVisible();

  await page.getByTestId("nav-meal").click();
  await page.getByTestId("meal-type").fill("dinner");
  await page.getByTestId("meal-calories").fill("720");
  const mealRespPromise = page.waitForResponse(
    (resp) => resp.url().includes("/api/v1/meals") && resp.request().method() === "POST"
  );
  await page.getByTestId("meal-submit").click();
  const mealResp = await mealRespPromise;
  expect(mealResp.ok()).toBeTruthy();

  await page.getByTestId("nav-emotion").click();
  await page.getByTestId("emotion-text").fill("stress fallback");
  await page.getByTestId("emotion-submit").click();
  await expect(page.getByTestId("emotion-result")).toContainText("emotionId");

  await page.getByTestId("nav-recommendation").click();
  await page.getByTestId("recommendation-submit").click();
  await expect(page.getByTestId("recommendation-result")).toContainText("adviceList");

  await page.getByTestId("nav-dashboard").click();
  await expect(page.getByTestId("dashboard-meal-count")).toContainText("mealCount");
});
