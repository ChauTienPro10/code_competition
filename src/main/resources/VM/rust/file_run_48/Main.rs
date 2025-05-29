use std::env;
use std::collections::HashMap;

fn main() {
    let args: Vec<String> = env::args().collect();

    if args.len() != 3 {
        return;
    }

    let nums_str = &args[1];
    let target: i32 = args[2].parse().expect("Target must be an integer");

    let nums: Vec<i32> = nums_str
        .split(',')
        .map(|s| s.trim().parse().expect("Invalid number in nums"))
        .collect();

    let result = two_sum(nums, target);
    println!("{:?}", result);
}

fn two_sum(nums: Vec<i32>, target: i32) -> Vec<i32> {
    // code cua ban o day
    vec![]
}