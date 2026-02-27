export function buildExplainText(rules) {
  if (!Array.isArray(rules) || rules.length === 0) {
    return "命中规则: R000";
  }
  return `命中规则: ${rules.join(", ")}`;
}
