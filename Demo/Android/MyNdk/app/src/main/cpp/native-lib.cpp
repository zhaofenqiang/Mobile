#include <jni.h>
#include "arm_compute/runtime/NEON/NEFunctions.h"
#include "arm_compute/core/Types.h"
#include "utils/Utils.h"

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_zfq_myndk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    using namespace arm_compute;
    using namespace utils;

    Image src_img;
    Image dst_img;
    Image gaus5x5_img;
    Image canny_edge_img;

    // Open PPM file
    PPMLoader ppm;
    ppm.open("img/dog.ppm");

    // Initialize just the dimensions and format of your buffers:
    ppm.init_image(src_img, Format::U8);

    // Initialize just the dimensions and format of the images:
    gaus5x5_img.allocator()->init(*src_img.info());
    canny_edge_img.allocator()->init(*src_img.info());
    dst_img.allocator()->init(*src_img.info());

    NEGaussian5x5             gaus5x5;
    NECannyEdge               canny_edge;
    NEArithmeticSubtraction   sub;

    // Configure the functions to call
    gaus5x5.configure(&src_img, &gaus5x5_img, BorderMode::REPLICATE);
    canny_edge.configure(&src_img, &canny_edge_img, 100, 80, 3, 1, BorderMode::REPLICATE);
    sub.configure(&gaus5x5_img, &canny_edge_img, &dst_img, ConvertPolicy::SATURATE);

    // Now that the padding requirements are known we can allocate the images:
    src_img.allocator()->allocate();
    dst_img.allocator()->allocate();
    gaus5x5_img.allocator()->allocate();
    canny_edge_img.allocator()->allocate();

    // Fill the input image with the content of the PPM image
    ppm.fill_image(src_img);

    // Execute the functions:
    gaus5x5.run();
    canny_edge.run();
    sub.run();

    // Save the result to file:
    save_to_ppm(dst_img, "img/cartoon_effect.ppm");

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}