package com.stripe;

public class BranchCoverageGlobalSetup {
  public static void setUpBeforeAllTests() {
    BranchCoverageUtil.writeDefault();
  }
}
