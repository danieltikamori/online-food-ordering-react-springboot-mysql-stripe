/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service;

import me.amlu.model.Category;

import java.util.List;

public interface CategoryService {

    public Category createCategory(String categoryName, Long userId) throws Exception;

    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception;

    public Category findCategoryById(Long categoryId) throws Exception;

    public Category findCategoryByName(String categoryName) throws Exception;

    Category findSimilarCategory(String categoryName);

    public Category updateCategory(Long categoryId, String categoryName, Long userId) throws Exception;

    public void deleteCategory(Long categoryId) throws Exception;

}
