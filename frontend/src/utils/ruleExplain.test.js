import { describe, expect, it } from "vitest";
import { buildExplainText } from "./ruleExplain";

describe("buildExplainText", () => {
  it("should return default when empty", () => {
    expect(buildExplainText([])).toBe("命中规则: R000");
  });

  it("should render joined rules", () => {
    expect(buildExplainText(["R001", "R002"])).toBe("命中规则: R001, R002");
  });
});
