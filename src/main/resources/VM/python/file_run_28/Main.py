import sys

def get_index_of_two_elm_equal_target(nums, target):
    # Code you here
    return []

if __name__ == "__main__":
    if len(sys.argv) < 3:
        sys.exit(1)
    num_strs = sys.argv[1].split(",")
    nums = list(map(int, num_strs))
    target = int(sys.argv[2])
    output = get_index_of_two_elm_equal_target(nums, target)
    print(output)
