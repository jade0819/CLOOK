import noCloudAfternoonBackground from "../assets/imgs/background/noCloudAfternoonBackground.jpg";
import noCloudNightBackground from "../assets/imgs/background/noCloudNightBackground.jpg";
import cloudAfternoonBackground from "../assets/imgs/background/cloudAfternoonBackground.jpg";
import cloudNightBackground from "../assets/imgs/background/cloudNightBackground.jpg";
import snowCloudyBackground from "../assets/imgs/background/snowCloudyBackground.jpg";
import rainCloudyBackground from "../assets/imgs/background/rainCloudyBackground.jpg";
import grayBackground from "../assets/imgs/background/grayBackground.jpg";

import sun from "../assets/imgs/icon/sun.png";
import cloud from "../assets/imgs/icon/cloud.png";
import moon from "../assets/imgs/icon/moon.png";
import moonCloud from "../assets/imgs/icon/moonCloud.png";
import rain from "../assets/imgs/icon/rain.png";
import sleet from "../assets/imgs/icon/sleet.png";
import snow from "../assets/imgs/icon/snow.png";
import sunCloud from "../assets/imgs/icon/sunCloud.png";

import character1 from "../assets/imgs/character/character1.png";

export function getWweatherImages(type, value) {
  let image = null;

  if (type === 1) {
    switch (value) {
      // background
      case "구름없는낮":
        image = noCloudAfternoonBackground;
        break;
      case "구름없는밤":
        image = noCloudNightBackground;
        break;
      case "구름많은낮":
        image = cloudAfternoonBackground;
        break;
      case "구름많은밤":
        image = cloudNightBackground;
        break;
      case "눈오는흐린배경":
        image = snowCloudyBackground;
        break;
      case "비오는흐린배경":
        image = rainCloudyBackground;
        break;
      case "흐린회색배경":
        image = grayBackground;
        break;
      default:
        image = grayBackground;
        break;
    }
  } else if (type === 2) {
    // character
    switch (value) {
      // background
      case "1":
        image = character1; // 기본A타입
        break;
      case "2":
        image = character1; // 기본B타입
        break;
      case "3":
        image = character1; // 더워하는
        break;
      case "4":
        image = character1; // 추워하는
        break;
      default:
        image = character1;
        break;
    }
  } else if (type === 3) {
    // icon
    switch (value) {
      case "해":
        image = sun;
        break;
      case "비":
        image = rain;
        break;
      case "눈":
        image = snow;
        break;
      case "진눈깨비":
        image = sleet;
        break;
      case "달":
        image = moon;
        break;
      case "구름":
        image = cloud;
        break;
      case "해구름":
        image = sunCloud;
        break;
      case "달구름":
        image = moonCloud;
        break;
      default:
        image = sun;
        break;
    }
  }

  return image;
}
