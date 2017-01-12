:: # Copyright 2013, Big Switch Networks, Inc.
:: #
:: # LoxiGen is licensed under the Eclipse Public License, version 1.0 (EPL), with
:: # the following special exception:
:: #
:: # LOXI Exception
:: #
:: # As a special exception to the terms of the EPL, you may distribute libraries
:: # generated by LoxiGen (LoxiGen Libraries) under the terms of your choice, provided
:: # that copyright and licensing notices generated by LoxiGen are not altered or removed
:: # from the LoxiGen Libraries and the notice provided below is (i) included in
:: # the LoxiGen Libraries, if distributed in source code form and (ii) included in any
:: # documentation for the LoxiGen Libraries, if distributed in binary form.
:: #
:: # Notice: "Copyright 2013, Big Switch Networks, Inc. This library was generated by the LoxiGen Compiler."
:: #
:: # You may not use this file except in compliance with the EPL or LOXI Exception. You may obtain
:: # a copy of the EPL at:
:: #
:: # http://www.eclipse.org/legal/epl-v10.html
:: #
:: # Unless required by applicable law or agreed to in writing, software
:: # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
:: # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
:: # EPL for the specific language governing permissions and limitations
:: # under the EPL.
:: include('_copyright.c')
#include <loci/loci.h>
#include <loci/of_object.h>
#include "loci_log.h"
#include "loci_int.h"

const char *const of_object_id_str[] = {
:: for cls in object_id_strs:
    "${cls}",
:: #endfor
};

const char *const of_version_str[] = {
    "Unknown OpenFlow Version",
    "OpenFlow-1.0",
    "OpenFlow-1.1",
    "OpenFlow-1.2"
};

const of_mac_addr_t of_mac_addr_all_ones = {
    {
        0xff, 0xff, 0xff, 0xff, 0xff, 0xff
    }
};
/* Just to be explicit; static duration vars are init'd to 0 */
const of_mac_addr_t of_mac_addr_all_zeros = {
    {
        0, 0, 0, 0, 0, 0
    }
};

const of_ipv6_t of_ipv6_all_ones = {
    {
        0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
        0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff
    }
};
/* Just to be explicit; static duration vars are init'd to 0 */
const of_ipv6_t of_ipv6_all_zeros = {
    {
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    }
};

const of_bitmap_256_t of_bitmap_256_all_ones = {
    { -1, -1, -1, -1, }
};

const of_bitmap_256_t of_bitmap_256_all_zeroes;

const of_bitmap_512_t of_bitmap_512_all_ones = {
    { -1, -1, -1, -1, -1, -1, -1, -1, }
};

const of_bitmap_512_t of_bitmap_512_all_zeroes;

/** @var of_error_strings
 * The error string map; use abs value to index
 */
const char *const of_error_strings[] = { OF_ERROR_STRINGS };
