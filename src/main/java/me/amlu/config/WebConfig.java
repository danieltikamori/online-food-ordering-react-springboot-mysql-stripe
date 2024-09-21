/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration public class WebConfig implements WebMvcConfigurer {

//   @Autowired
   private final SoftDeleteFilterInterceptor softDeleteFilterInterceptor;

   public WebConfig(SoftDeleteFilterInterceptor softDeleteFilterInterceptor) {
       this.softDeleteFilterInterceptor = softDeleteFilterInterceptor;
   }

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(softDeleteFilterInterceptor);
   }
}