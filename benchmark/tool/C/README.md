# Inference demo

This is an inference demo program based on the C API of [PaddleOnACL](https://github.com/zhaofenqiang/PaddleOnACL).
The demo can be run from the command line and can be used to test the inference performance of various different models.

# Run it on Raspberry Pi 3

- **Step 1, [Build PadlleOnACL for Raspberry Pi 3](https://github.com/zhaofenqiang/PaddleOnACL/blob/develop/Installation.md#build-for-raspberry-pi)**

- **Step 2, Copy the shared library and related head files to Raspberry Pi**
- **Step 3, build the inference demo.**  
Compile `inference.cc` to an executable program for the Raspberry Pi environment as follows:
    ```
    arm-linux-gnueabihf-g++ inference.cpp -ICAPI_RPI/include -std=c++11 -LpaddleonACL -LComputeLibrary/lib -lpaddle_capi_shared -larm_compute -larm_compute_core -o inference -g
    ```
    Make sure you can find the library using `-L` `-l`

- **Step 4, [prepare a merged model](https://github.com/PaddlePaddle/Mobile/blob/develop/benchmark/tool/C/README.md)**

- **Step 5, run the demo.**
    ```
    export LD_LIBRARY_PATH=/home/zfq/mobile/paddleonACL:/home/zfq/mobile/ComputeLibrary/lib
    ./inference --merged_model ./mobilenet_flowers102.paddle --input_size 150528
    ```
    **Note:** `input_size` is 150528, cause that the input size of the model is `3 * 224 * 224 = 150528`

# Run it on Android
Please following this [guide](https://github.com/PaddlePaddle/Mobile/blob/develop/benchmark/tool/C/README.md), just replace the shared and static library.
